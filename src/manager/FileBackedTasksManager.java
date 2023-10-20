package manager;

import manager.exceptions.ManagerSaveException;
import manager.exceptions.ManagerUploadException;
import tasks.*;
import tasks.taskConditions.Status;
import tasks.taskConditions.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path filePath;
    private final String HEADER = "id,type,name,status,description,epic,startTime,duration,endTime\n";

    public FileBackedTasksManager(String path) {
        filePath = Paths.get(path);
    }

    protected FileBackedTasksManager() {
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()))) {
            writer.write(HEADER);
            for (Task task : super.getTasks()) {
                writer.write(taskToString(task) + "\n");
            }
            for (Epic epic : super.getEpics()) {
                writer.write(taskToString(epic) + "\n");
            }
            for (Subtask subtask : super.getSubtasks()) {
                writer.write(taskToString(subtask) + "\n");
            }
            writer.write("\n" + historyToString(history));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager newManager = new FileBackedTasksManager(path);
        List<Integer> historyId = new ArrayList<>();
        try {
            List<String> strings = Files.readAllLines(Paths.get(path));
            boolean startHistory = false;
            for (int i = 1; i < strings.size(); i++) {
                if (!startHistory) {
                    if (strings.get(i).isBlank()) {
                        startHistory = true;
                    } else newManager.taskFromString(strings.get(i));
                    continue;
                }
                historyId = newManager.historyFromString(strings.get(i));
            }
        } catch (IOException e) {
            throw new ManagerUploadException();
        }
        for (int id : historyId) {
            if (newManager.tasks.containsKey(id)) newManager.history.add(newManager.tasks.get(id));
            else if (newManager.epics.containsKey(id)) newManager.history.add(newManager.epics.get(id));
            else newManager.history.add(newManager.subtasks.get(id));
        }
        return newManager;
    }

    private String taskToString(Task task) {
        String epicId = "";
        String endTime = "";
        final TaskType type = task.getType();
        if (type.equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            epicId = subtask.getEpicId().toString();
        }
        if (type.equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
            endTime = epic.getEndTime().toString();
        }
        return String.join(",", task.getId().toString(), type.toString(),
                task.getName(), task.getStatus().toString(), task.getDescription(), epicId, task.getStartTime().toString(), Long.toString(task.getDuration().toMinutes()), endTime);
    }

    private void taskFromString(String value) {
        String[] taskValues = value.split(",");
        int id = Integer.parseInt(taskValues[0]);
        if (id >= generatedId) generatedId = id + 1;
        final TaskType type = TaskType.valueOf(taskValues[1]);
        switch (type) {
            case TASK:
                Task newTask = new Task(taskValues[2], taskValues[4], LocalDateTime.parse(taskValues[6]), Long.parseLong(taskValues[7]));
                newTask.setId(id);
                newTask.setStatus(Status.valueOf(taskValues[3]));
                addTask(newTask);
                break;
            case EPIC:
                Epic newEpic = new Epic(taskValues[2], taskValues[4]);
                newEpic.setId(id);
                newEpic.setStatus(Status.valueOf(taskValues[3]));
                newEpic.setStartTime(LocalDateTime.parse(taskValues[6]));
                newEpic.setEndTime(LocalDateTime.parse(taskValues[8]));
                newEpic.setDuration(Duration.ofMinutes(Long.parseLong(taskValues[7])));
                addEpic(newEpic);
                break;
            case SUBTASK:
                Subtask newSubtask = new Subtask(taskValues[2], taskValues[4], Integer.parseInt(taskValues[5]), LocalDateTime.parse(taskValues[6]), Long.parseLong(taskValues[7]));
                newSubtask.setId(id);
                newSubtask.setStatus(Status.valueOf(taskValues[3]));
                addSubtask(newSubtask);
        }
    }

    private String historyToString(HistoryManager manager) {
        List<String> historyId = manager.getHistory().stream()
                .map(task -> task.getId().toString())
                .collect(Collectors.toList());
        Collections.reverse(historyId);
        return String.join(",", historyId);
    }

    private List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    protected void addTask(Task task) {
        tasks.put(task.getId(), task);
        sortedTasks.add(task);
    }

    protected void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        sortedTasks.add(subtask);
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }
}