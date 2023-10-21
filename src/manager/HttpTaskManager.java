package manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kvServer.KVServer;
import manager.jsonTransformation.TaskGson;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private KVServer server;
    private final String serverURL;
    private final Gson gson;
    private final Type listType;
    private final Type listTypeEpic;
    private final Type listTypeSubtask;
    private final Type listTypeHistory;

    public HttpTaskManager(String serverURL) {
        this.serverURL = serverURL;
        client = setUpClient();
        gson = TaskGson.getTaskGson();
        listType = new TypeToken<List<Task>>() {
        }.getType();
        listTypeEpic = new TypeToken<List<Epic>>() {
        }.getType();
        listTypeSubtask = new TypeToken<List<Subtask>>() {
        }.getType();
        listTypeHistory = new TypeToken<List<Integer>>() {
        }.getType();
    }

    @Override
    protected void save() {
        if (client != null) {
            client.put("tasks", gson.toJson(getTasks()));
            client.put("epics", gson.toJson(getEpics()));
            client.put("subtasks", gson.toJson(getSubtasks()));
            client.put("history", gson.toJson(getHistoryAsIdList()));
        } else {
            client = setUpClient();
            save();
        }
    }

    public static HttpTaskManager loadFromServer() {
        HttpTaskManager taskManager = Managers.getDefault();
        List<Task> newTasks = taskManager.gson.fromJson(taskManager.client.load("tasks"), taskManager.listType);
        List<Epic> newEpics = taskManager.gson.fromJson((taskManager.client.load("epics")), taskManager.listTypeEpic);
        List<Subtask> newSubtasks = taskManager.gson.fromJson((taskManager.client.load("subtasks")), taskManager.listTypeSubtask);
        List<Integer> newHistory = taskManager.gson.fromJson((taskManager.client.load("history")), taskManager.listTypeHistory);
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
        for (int id : newHistory) {
            if (taskManager.tasks.containsKey(id)) taskManager.history.add(taskManager.getTaskById(id));
            else if (taskManager.epics.containsKey(id)) taskManager.history.add(taskManager.getEpicById(id));
            else taskManager.history.add(taskManager.getSubtaskById(id));
        }
        return taskManager;
    }

    private List<Integer> getHistoryAsIdList() {
        List<Integer> ids = new ArrayList<>();
        for (Task task : getHistory()) {
            ids.add(task.getId());
        }
        return ids;
    }

    public void stopInnerServer() {
        if (server != null) {
            server.stop();
            server = null;
            client = null;
        }
    }

    private KVTaskClient setUpClient() {
        try {
            return new KVTaskClient(serverURL);
        } catch (RuntimeException runtimeException) {
            try {
                server = new KVServer();
                server.start();
            } catch (IOException ioException) {
                throw new RuntimeException("Сервера под таким URL нет");
            }
            return new KVTaskClient(serverURL);
        }
    }
}