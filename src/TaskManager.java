import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int generatedId = 1;

    TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else return null;
    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else return null;
    }

    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else return null;
    }

    public void createTask(Task task) {
        if (task != null) {
            task.id = generatedId++;
            task.status = Status.New;
            tasks.put(task.id, task);
        }
    }

    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.id = generatedId++;
            epic.status = Status.New;
            epics.put(epic.id, epic);
        }
    }

    public void createSubtask(Subtask subtask) {
        if (subtask != null && epics.containsKey(subtask.getEpicId())) {
            subtask.id = generatedId++;
            subtask.status = Status.New;
            subtasks.put(subtask.id, subtask);
            epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.id);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.id)) {
            tasks.remove(task.id);
            task.id = generatedId++;
            tasks.put(task.id, task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.id)) {
            epics.remove(epic.id);
            epic.id = generatedId++;
            epics.put(epic.id, epic);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                if (subtasks.containsKey(subtaskId)) subtasks.get(subtaskId).setEpicId(epic.id);
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.id)) {
            subtasks.remove(subtask.id);
            epics.get(subtask.getEpicId()).getSubtaskIds().remove(subtask.id);
            subtask.id = generatedId++;
            subtasks.put(subtask.id, subtask);
            epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.id);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtaskIds()) {
                if (subtasks.containsKey(subtaskId)) subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).getSubtaskIds().remove(id);
            updateEpicStatus(epics.get(subtasks.get(id).getEpicId()));
            subtasks.remove(id);
        }
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Subtask> subtasksList = new ArrayList<>();
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
                if (subtasks.containsKey(subtaskId)) subtasksList.add(subtasks.get(subtaskId));
            }
            return subtasksList;
        } else return null;
    }

    private void updateEpicStatus(Epic epic) {
        int doneSubtasks = 0;
        int newSubtasks = 0;
        int subtaskCount = epic.getSubtaskIds().size();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            if (subtasks.get(subtaskId).status == Status.New) newSubtasks++;
            else doneSubtasks++;
        }
        if (newSubtasks == subtaskCount) epic.status = Status.New;
        else if (doneSubtasks == subtaskCount) epic.status = Status.DONE;
        else epic.status = Status.IN_PROGRESS;
    }
}
