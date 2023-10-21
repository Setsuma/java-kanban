package tests;

import com.google.gson.Gson;
import kvServer.KVServer;
import manager.HttpTaskManager;
import manager.HttpTaskServer;
import manager.jsonTransformation.TaskGson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.taskConditions.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private HttpTaskManager taskManager;
    private static HttpClient client = HttpClient.newHttpClient();
    private static Gson gson = TaskGson.getTaskGson();

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer();
        taskManager = httpTaskServer.getTaskManager();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    void getAllTasksEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8079/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getPrioritizedTasks()), response.body());
    }

    @Test
    void getTaskByIdEndpoint() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        taskManager.createTask(task);

        URI url = URI.create("http://localhost:8079/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getTaskById(1)), response.body());
    }

    @Test
    void getTasksEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        URI url = URI.create("http://localhost:8079/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getTasks()), response.body());
    }

    @Test
    void getEpicByIdEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8079/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getEpicById(1)), response.body());
    }

    @Test
    void getEpicsEndpoint() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);
        taskManager.createEpic(epic2);

        URI url = URI.create("http://localhost:8079/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getEpics()), response.body());
    }

    @Test
    void getSubtaskByIdEndpoint() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8079/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getSubtaskById(2)), response.body());
    }

    @Test
    void getSubtasksEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8079/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getSubtasks()), response.body());
    }

    @Test
    void getSubtasksByEpicIdEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8079/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getSubtasksByEpicId(1)), response.body());
    }

    @Test
    void getHistoryEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(4);

        URI url = URI.create("http://localhost:8079/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getHistory()), response.body());
    }

    @Test
    void postTaskCreateEndpoint() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        URI url = URI.create("http://localhost:8079/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        task.setId(1);
        task.setStatus(Status.NEW);
        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void postTaskUpdateEndpoint() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        URI url = URI.create("http://localhost:8079/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        task.setId(1);
        task.setStatus(Status.IN_PROGRESS);
        task.setName("Обновленная задача 1");

        String update = gson.toJson(task);
        final HttpRequest.BodyPublisher bodyUpdate = HttpRequest.BodyPublishers.ofString(update);
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(url).POST(bodyUpdate).build();
        HttpResponse<String> responseUpdate = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseUpdate.statusCode());
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    void postEpicCreateEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");

        URI url = URI.create("http://localhost:8079/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        epic.setId(1);
        epic.setStatus(Status.NEW);
        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void postEpicUpdateEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");

        URI url = URI.create("http://localhost:8079/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        epic.setId(1);
        epic.setName("Обновленный эпик 1");

        String update = gson.toJson(epic);
        final HttpRequest.BodyPublisher bodyUpdate = HttpRequest.BodyPublishers.ofString(update);
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(url).POST(bodyUpdate).build();
        HttpResponse<String> responseUpdate = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());

        epic.setStatus(Status.NEW);

        assertEquals(201, responseUpdate.statusCode());
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void postSubtaskCreateEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createEpic(epic);

        URI url = URI.create("http://localhost:8079/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        subtask.setId(2);
        subtask.setStatus(Status.NEW);
        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(subtask, taskManager.getSubtaskById(2));
    }

    @Test
    void postSubtaskUpdateEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createEpic(epic);

        URI url = URI.create("http://localhost:8079/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        subtask.setId(2);
        epic.setName("Обновленная подзадача 1");

        String update = gson.toJson(subtask);
        final HttpRequest.BodyPublisher bodyUpdate = HttpRequest.BodyPublishers.ofString(update);
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(url).POST(bodyUpdate).build();
        HttpResponse<String> responseUpdate = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(subtask, taskManager.getSubtaskById(2));
    }

    @Test
    void deleteTaskByIdEndpoint() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        taskManager.createTask(task);

        URI url = URI.create("http://localhost:8079/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteTasksEndpoint() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        URI url = URI.create("http://localhost:8079/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteEpicByIdEndpoint() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);

        URI url = URI.create("http://localhost:8079/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void deleteEpicsEndpoint() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        URI url = URI.create("http://localhost:8079/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void deleteSubtaskByIdEndpoint() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        taskManager.createSubtask(subtask1);

        URI url = URI.create("http://localhost:8079/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void deleteSubtasksEndpoint() throws IOException, InterruptedException {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8079/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void requestWithInvalidMethodPUT() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        URI url = URI.create("http://localhost:8079/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void requestWithInvalidURL() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        URI url = URI.create("http://localhost:8079/epics/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void requestWithInvalidJson() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        URI url = URI.create("http://localhost:8079/tasks/task/");
        String json = "null";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void requestWithInvalidQuery() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        taskManager.createTask(task);

        URI url = URI.create("http://localhost:8079/tasks/task/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}