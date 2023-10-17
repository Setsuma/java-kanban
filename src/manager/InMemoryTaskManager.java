package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final TreeSet<Task> sortedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime().equals(o2.getStartTime())) return 0;
        if (o1.getStartTime() == null) return -1;
        else if (o2.getStartTime() == null) return 1;
        else if (o1.getStartTime().isAfter(o2.getStartTime())) return 1;
        else return -1;
    });
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected int generatedId = 1;

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() {
        for (int id : tasks.keySet()) {
            history.remove(id);
            sortedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epics.values()) {
            for (int id : epic.getSubtasksIds()) {
                history.remove(id);
                sortedTasks.remove(subtasks.get(id));
            }
            history.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            for (int id : epic.getSubtasksIds()) {
                history.remove(id);
                sortedTasks.remove(subtasks.get(id));
            }
            epic.cleanSubtaskIds();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        history.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        history.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        if (task != null && isNotIntersects(task)) {
            task.setId(generatedId++);
            task.setStatus(Status.NEW);
            tasks.put(task.getId(), task);
            sortedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(generatedId++);
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask != null && epics.containsKey(subtask.getEpicId()) && isNotIntersects(subtask)) {
            subtask.setId(generatedId++);
            subtask.setStatus(Status.NEW);
            subtasks.put(subtask.getId(), subtask);
            sortedTasks.add(subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId()) && isNotIntersects(task)) {
            sortedTasks.remove(tasks.get(task.getId()));
            tasks.put(task.getId(), task);
            sortedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())
                && epics.get(subtask.getEpicId()).getSubtasksIds().contains(subtask.getId()) && isNotIntersects(subtask)) {
            sortedTasks.remove(subtasks.get(subtask.getId()));
            subtasks.put(subtask.getId(), subtask);
            sortedTasks.add(subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            sortedTasks.remove(tasks.get(id));
            tasks.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (Integer subtaskId : epic.getSubtasksIds()) {
                sortedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                history.remove(subtaskId);
            }
            history.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            sortedTasks.remove(subtasks.get(id));
            epics.get(subtasks.get(id).getEpicId()).removeSubtask(id);
            updateEpicStatus(epics.get(subtasks.get(id).getEpicId()));
            subtasks.remove(id);
            history.remove(id);
        }
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    protected boolean isNotIntersects(Task newTask) {
        for (Task task : sortedTasks) {
            if (task.getId().equals(newTask.getId())) continue;
            if (!newTask.getEndTime().isAfter(task.getStartTime())) {// newTimeEnd <= existTimeStart
                continue;
            }
            if (!task.getEndTime().isAfter(newTask.getStartTime())) {// existTimeEnd <= newTimeStart
                continue;
            }
            return false;
        }
        return true;
    }

    protected void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subs = epic.getSubtasksIds();
        if (subs.isEmpty()) {
            epic.setStatus(Status.NEW);
            epic.setStartTime(LocalDateTime.now());
            epic.setEndTime(LocalDateTime.now());
            epic.setDuration(Duration.ofMinutes(0));
            return;
        }
        Status status = null;
        long minutes = 0;
        LocalDateTime first = subtasks.get(subs.get(0)).getStartTime();
        LocalDateTime last = subtasks.get(subs.get(0)).getEndTime();
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            if (subtask.getStartTime().isBefore(first)) first = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(last)) last = subtask.getEndTime();
            minutes += subtask.getDuration().toMinutes();
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
        epic.setStartTime(first);
        epic.setEndTime(last);
        epic.setDuration(Duration.ofMinutes(minutes));
        epic.setStatus(status);
    }

    public int getGeneratedId() {
        return generatedId;
    }
}
