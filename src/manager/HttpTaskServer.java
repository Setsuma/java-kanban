package manager;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import manager.jsonTransformation.TaskGson;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    private final int PORT = 8079;
    private final HttpTaskManager taskManager;
    private final HttpServer server;
    public static Gson gson = TaskGson.getTaskGson();

    public HttpTaskServer() throws IOException {
        taskManager = HttpTaskManager.loadFromServer();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.start();
        System.out.println("taskServer started on port " + PORT);
    }

    public void stop() {
        System.out.println("TaskServer stopped");
        server.stop(0);
    }

    public HttpTaskManager getTaskManager(){
        return taskManager;
    }

    static class TaskHandler implements HttpHandler {
        private final HttpTaskManager taskManager;

        public TaskHandler(HttpTaskManager taskManager) {
            this.taskManager = taskManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathSegments = path.split("/");
            boolean isQueryStringExists = exchange.getRequestURI().getQuery() != null;
            switch (method) {
                case "GET":
                    if (pathSegments.length == 2 || pathSegments[2].isBlank()) {
                        handleGetAllTasks(exchange);
                    } else if (pathSegments[2].equals("task")) {
                        if (isQueryStringExists)
                            handleGetTaskById(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                        else handleGetTasks(exchange);
                    } else if (pathSegments[2].equals("epic")) {
                        if (isQueryStringExists)
                            handleGetEpicById(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                        else handleGetEpics(exchange);
                    } else if (pathSegments[2].equals("subtask")) {
                        if (isQueryStringExists) {
                            if (pathSegments.length == 4 && pathSegments[3].equals("epic"))
                                handleGetSubtasksByEpicId(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                            else handleGetSubtaskById(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                        } else handleGetSubtasks(exchange);
                    } else if (pathSegments[2].equals("history")) {
                        handleGetHistory(exchange);
                    } else handleNotFound(exchange);
                    break;
                case "POST":
                    if (pathSegments[2].equals("task")) {
                        handleCreateTask(exchange);
                    } else if (pathSegments[2].equals("epic")) {
                        handleCreateEpic(exchange);
                    } else if (pathSegments[2].equals("subtask")) {
                        handleCreateSubtask(exchange);
                    } else handleNotFound(exchange);
                    break;
                case "DELETE":
                    if (pathSegments[2].equals("task")) {
                        if (isQueryStringExists)
                            handleDeleteTaskById(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                        else handleDeleteTasks(exchange);
                    } else if (pathSegments[2].equals("epic")) {
                        if (isQueryStringExists)
                            handleDeleteEpicById(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                        else handleDeleteEpics(exchange);
                    } else if (pathSegments[2].equals("subtask")) {
                        if (isQueryStringExists)
                            handleDeleteSubtaskById(exchange, getIdFromQuery(exchange.getRequestURI().getQuery()));
                        else handleDeleteSubtasks(exchange);
                    } else handleNotFound(exchange);
                    break;
                default:
                    handleMethodNotAllowed(exchange);
            }
        }

        private int getIdFromQuery(String query) {
            String[] queryParams = query.split("=");
            if (queryParams.length == 2 && queryParams[0].equals("id")) {
                try {
                    int id = Integer.parseInt(queryParams[1]);
                    if (id > 0) return id;
                    else return -1;
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
            return -1;
        }

        private void handleGetAllTasks(HttpExchange exchange) throws IOException {
            List<Task> tasks = taskManager.getPrioritizedTasks();
            String response = gson.toJson(tasks);
            sendResponse(exchange, 200, response);
        }

        private void handleGetTaskById(HttpExchange exchange, int taskId) throws IOException {
            if (taskId == -1) handleNotFound(exchange);
            else {
                Task task = taskManager.getTaskById(taskId);
                if (task != null) {
                    String response = gson.toJson(task);
                    sendResponse(exchange, 200, response);
                } else handleNotFound(exchange);
            }
        }

        private void handleGetTasks(HttpExchange exchange) throws IOException {
            List<Task> tasks = taskManager.getTasks();
            String response = gson.toJson(tasks);
            sendResponse(exchange, 200, response);
        }


        private void handleGetEpicById(HttpExchange exchange, int epicId) throws IOException {
            if (epicId == -1) handleNotFound(exchange);
            else {
                Epic epic = taskManager.getEpicById(epicId);
                if (epic != null) {
                    String response = gson.toJson(epic);
                    sendResponse(exchange, 200, response);
                } else handleNotFound(exchange);
            }
        }

        private void handleGetEpics(HttpExchange exchange) throws IOException {
            List<Epic> epics = taskManager.getEpics();
            String response = gson.toJson(epics);
            sendResponse(exchange, 200, response);
        }


        private void handleGetSubtaskById(HttpExchange exchange, int subtaskId) throws IOException {
            if (subtaskId == -1) handleNotFound(exchange);
            else {
                Subtask subtask = taskManager.getSubtaskById(subtaskId);
                if (subtask != null) {
                    String response = gson.toJson(subtask);
                    sendResponse(exchange, 200, response);
                } else handleNotFound(exchange);
            }
        }

        private void handleGetSubtasksByEpicId(HttpExchange exchange, int epicId) throws IOException {
            if (epicId == -1) handleNotFound(exchange);
            else {
                if (taskManager.getSubtasksByEpicId(epicId) != null) {
                    List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);
                    String response = gson.toJson(subtasks);
                    sendResponse(exchange, 200, response);
                } else handleNotFound(exchange);
            }
        }

        private void handleGetSubtasks(HttpExchange exchange) throws IOException {
            List<Subtask> subtasks = taskManager.getSubtasks();
            String response = gson.toJson(subtasks);
            sendResponse(exchange, 200, response);
        }


        private void handleGetHistory(HttpExchange exchange) throws IOException {
            List<Task> history = taskManager.getHistory();
            String response = gson.toJson(history);
            sendResponse(exchange, 200, response);
        }

        private void handleCreateTask(HttpExchange exchange) throws IOException {
            String requestBody = getRequestString(exchange);
            Task task = gson.fromJson(requestBody, Task.class);
            if (task != null) {
                if (taskManager.tasks.containsKey(task.getId())) {
                    taskManager.updateTask(task);
                    sendResponse(exchange, 201, "Task updated");
                } else {
                    taskManager.createTask(task);
                    sendResponse(exchange, 201, "Task created");
                }
            } else {
                handleBadRequest(exchange, "Invalid task data");
            }
        }

        private void handleCreateEpic(HttpExchange exchange) throws IOException {
            String requestBody = getRequestString(exchange);
            Epic epic = gson.fromJson(requestBody, Epic.class);
            if (epic != null) {
                if (taskManager.epics.containsKey(epic.getId())) {
                    taskManager.updateEpic(epic);
                    sendResponse(exchange, 201, "Epic updated");
                } else {
                    taskManager.createEpic(epic);
                    sendResponse(exchange, 201, "Epic created");
                }
            } else {
                handleBadRequest(exchange, "Invalid epic data");
            }
        }

        private void handleCreateSubtask(HttpExchange exchange) throws IOException {
            String requestBody = getRequestString(exchange);
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            if (subtask != null) {
                if (taskManager.subtasks.containsKey(subtask.getId())) {
                    taskManager.updateSubtask(subtask);
                    sendResponse(exchange, 201, "Subtask updated");
                } else {
                    taskManager.createSubtask(subtask);
                    sendResponse(exchange, 201, "Subtask created");
                }
            } else {
                handleBadRequest(exchange, "Invalid subtask data");
            }
        }

        private void handleDeleteTaskById(HttpExchange exchange, int taskId) throws IOException {
            if (taskId == -1) handleNotFound(exchange);
            else {
                taskManager.deleteTaskById(taskId);
                sendResponse(exchange, 200, "Task deleted");
            }
        }

        private void handleDeleteTasks(HttpExchange exchange) throws IOException {
            taskManager.clearTasks();
            sendResponse(exchange, 200, "Tasks deleted");
        }

        private void handleDeleteEpicById(HttpExchange exchange, int epicId) throws IOException {
            if (epicId == -1) handleNotFound(exchange);
            else {
                taskManager.deleteEpicById(epicId);
                sendResponse(exchange, 200, "Epic deleted");
            }
        }

        private void handleDeleteEpics(HttpExchange exchange) throws IOException {
            taskManager.clearEpics();
            sendResponse(exchange, 200, "Epics deleted");
        }

        private void handleDeleteSubtaskById(HttpExchange exchange, int subtaskId) throws IOException {
            taskManager.deleteSubtaskById(subtaskId);
            sendResponse(exchange, 200, "Subtask deleted");
        }

        private void handleDeleteSubtasks(HttpExchange exchange) throws IOException {
            taskManager.clearSubtasks();
            sendResponse(exchange, 200, "Subtasks deleted");
        }

        private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
            sendResponse(exchange, 405, "Method Not Allowed");
        }

        private void handleBadRequest(HttpExchange exchange, String message) throws IOException {
            sendResponse(exchange, 400, message);
        }

        private static void handleNotFound(HttpExchange exchange) throws IOException {
            sendResponse(exchange, 404, "Not Found");
        }

        private String getRequestString(HttpExchange exchange) throws IOException {
            InputStream requestBody = exchange.getRequestBody();
            byte[] requestBodyBytes = requestBody.readAllBytes();
            return new String(requestBodyBytes, StandardCharsets.UTF_8);
        }

        private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseBytes);
            responseBody.close();
        }
    }
}
