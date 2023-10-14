package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path filePath;

    public FileBackedTasksManager(String path) {
        filePath = Paths.get(path);
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()))) {
            writer.write("id,type,name,status,description,epic\n");
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

    public void upload() {
        List<Integer> historyId = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            boolean startHistory = false;
            while ((line = reader.readLine()) != null) {
                if (!startHistory) {
                    if (line.isBlank()) {
                        startHistory = true;
                    } else if (!line.equals("id,type,name,status,description,epic")) {
                        taskFromString(line);
                    }
                    continue;
                }
                historyId = historyFromString(line);
            }
        } catch (IOException e) {
            throw new ManagerUploadException();
        }
        for (int id : historyId) {
            if (tasks.containsKey(id)) history.add(tasks.get(id));
            else if (epics.containsKey(id)) history.add(epics.get(id));
            else history.add(subtasks.get(id));
        }
    }

    private String taskToString(Task task) {
        String epicId = "";
        String type = task.getClass().getSimpleName().toUpperCase();
        if (type.equals("SUBTASK")) {
            Subtask subtask = (Subtask) task;
            epicId = subtask.getEpicId().toString();
        }
        return String.join(",", task.getId().toString(), type,
                task.getName(), task.getStatus().toString(), task.getDescription(), epicId);
    }

    private void taskFromString(String value) {
        String[] taskValues = value.split(",");
        int id = Integer.parseInt(taskValues[0]);
        if (id >= generatedId) generatedId = id + 1;
        switch (taskValues[1]) {
            case "TASK":
                Task newTask = new Task(taskValues[2], taskValues[4]);
                newTask.setId(id);
                newTask.setStatus(Status.valueOf(taskValues[3]));
                addTask(newTask);
                break;
            case "EPIC":
                Epic newEpic = new Epic(taskValues[2], taskValues[4]);
                newEpic.setId(id);
                newEpic.setStatus(Status.valueOf(taskValues[3]));
                addEpic(newEpic);
                break;
            case "SUBTASK":
                Subtask newSubtask = new Subtask(taskValues[2], taskValues[4], Integer.parseInt(taskValues[5]));
                newSubtask.setId(id);
                newSubtask.setStatus(Status.valueOf(taskValues[3]));
                addSubtask(newSubtask);
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<String> historyId = manager.getHistory().stream()
                .map(task -> task.getId().toString())
                .collect(Collectors.toList());
        Collections.reverse(historyId);
        return String.join(",", historyId);
    }

    public static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    private void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        super.updateEpic(epics.get(subtask.getEpicId()));
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return super.getSubtasks();
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

    @Override
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        return super.getSubtasksByEpicId(epicId);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
