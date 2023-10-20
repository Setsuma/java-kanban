package manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import manager.jsonTransformation.TaskGson;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;
    private final Type listType;
    private final Type listTypeEpic;
    private final Type listTypeSubtask;

    public HttpTaskManager(String serverURL) {
        client = new KVTaskClient(serverURL);
        gson = TaskGson.getTaskGson();
        listType = new TypeToken<List<Task>>() {
        }.getType();
        listTypeEpic = new TypeToken<List<Epic>>() {
        }.getType();
        listTypeSubtask = new TypeToken<List<Subtask>>() {
        }.getType();
    }

    @Override
    protected void save() {
        client.put("tasks", gson.toJson(getTasks()));
        client.put("epics", gson.toJson(getEpics()));
        client.put("subtasks", gson.toJson(getSubtasks()));
        client.put("history", gson.toJson(history.getHistory()));
    }

    public static HttpTaskManager loadFromServer() {
        HttpTaskManager taskManager = Managers.getDefault();
        List<Task> newTasks = taskManager.gson.fromJson(taskManager.client.load("tasks"), taskManager.listType);
        List<Epic> newEpics = taskManager.gson.fromJson((taskManager.client.load("epics")), taskManager.listTypeEpic);
        List<Subtask> newSubtasks = taskManager.gson.fromJson((taskManager.client.load("subtasks")), taskManager.listTypeSubtask);
        List<Task> newHistory = taskManager.gson.fromJson((taskManager.client.load("history")), taskManager.listType);
        Collections.reverse(newHistory);

        for (Task task : newTasks) {
            if (task.getId() >= taskManager.generatedId) taskManager.generatedId = task.getId() + 1;
            taskManager.addTask(task);
        }
        for (Epic epic : newEpics) {
            if (epic.getId() >= taskManager.generatedId) taskManager.generatedId = epic.getId() + 1;
            taskManager.addEpic(epic);
        }
        for (Subtask subtask : newSubtasks) {
            if (subtask.getId() >= taskManager.generatedId) taskManager.generatedId = subtask.getId() + 1;
            taskManager.addSubtask(subtask);
        }
        for (Task task : newHistory) {
            int id = task.getId();
            if (taskManager.tasks.containsKey(id)) taskManager.history.add(task);
            else taskManager.history.add(taskManager.subtasks.get(id));
        }
        return taskManager;
    }
}