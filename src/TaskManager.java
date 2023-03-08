import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    final private HashMap<Integer, Task> tasks = new HashMap<>();
    final private HashMap<Integer, Epic> epics = new HashMap<>();
    final private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatedId = 1;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        }
        return null;
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(generatedId++);
            task.setStatus(Status.New);
            tasks.put(task.id, task);
        }
    }

    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(generatedId++);
            epic.setStatus(Status.New);
            epics.put(epic.id, epic);
        }
    }

    public void createSubtask(Subtask subtask) {
        if (subtask != null && epics.containsKey(subtask.getEpicId())) {
            subtask.setId(generatedId++);
            subtask.setStatus(Status.New);
            subtasks.put(subtask.id, subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(subtask.id);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.id)) {
            tasks.put(task.id, task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.id)) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.id) && epics.containsKey(subtask.getEpicId())
                && epics.get(subtask.getEpicId()).getSubtasksIds().contains(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).removeSubtask(id);
            updateEpicStatus(epics.get(subtasks.get(id).getEpicId()));
            subtasks.remove(id);
        }
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Subtask> subtasksList = new ArrayList<>();
            for (Integer subtaskId : epics.get(epicId).getSubtasksIds()) {
                if (subtasks.containsKey(subtaskId)) subtasksList.add(subtasks.get(subtaskId));
            }
            return subtasksList;
        }
        return null;
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subs = epic.getSubtasksIds();
        if (subs.isEmpty()) {
            epic.setStatus(Status.New);
            return;
        }
        Status status = null;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            if (status == null) {
                status = subtask.getStatus();
                continue;
            }
            if (status.equals(subtask.getStatus())
                    && status != Status.IN_PROGRESS) {
                continue;
            }
            epic.setStatus(Status.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }
}
