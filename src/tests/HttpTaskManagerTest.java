package tests;

import kvServer.KVServer;
import manager.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private static KVServer kvServer;

    @Override
    protected HttpTaskManager createTaskManager() throws IOException {
        return new HttpTaskManager("http://localhost:8078");
    }

    @AfterEach
    public void shutDown() {
        taskManager.stopInnerServer();
    }

    @Test
    public void saveAndLoadWithStandardBehavior() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 2, LocalDateTime.of(2025, 5, 27, 18, 30), 30);
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        List<Task> history = taskManager.getHistory();

        HttpTaskManager loadedTasks = HttpTaskManager.loadFromServer();
        List<Task> tasks = loadedTasks.getTasks();
        List<Epic> epics = loadedTasks.getEpics();
        List<Subtask> subtasks = loadedTasks.getSubtasks();
        List<Task> loadedHistory = loadedTasks.getHistory();
        assertEquals(1, tasks.size());
        assertEquals(1, epics.size());
        assertEquals(1, subtasks.size());
        assertEquals(2, loadedHistory.size());
        assertEquals(4, loadedTasks.getGeneratedId());
        assertEquals(taskManager.getTaskById(1), loadedTasks.getTaskById(1));
        assertEquals(taskManager.getEpicById(2), loadedTasks.getEpicById(2));
        assertEquals(taskManager.getSubtaskById(3), loadedTasks.getSubtaskById(3));
        assertEquals(history, loadedHistory);
    }

    @Test
    public void saveAndLoadEmptyTaskList() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();

        HttpTaskManager loadedTasks = HttpTaskManager.loadFromServer();
        List<Task> tasks = loadedTasks.getTasks();
        List<Epic> epics = loadedTasks.getEpics();
        List<Subtask> subtasks = loadedTasks.getSubtasks();
        List<Task> history = loadedTasks.getHistory();
        assertEquals(0, tasks.size());
        assertEquals(0, epics.size());
        assertEquals(0, subtasks.size());
        assertEquals(0, history.size());
        assertEquals(1, loadedTasks.getGeneratedId());
    }

    @Test
    public void saveAndLoadEpicWithoutSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);

        HttpTaskManager loadedTasks = HttpTaskManager.loadFromServer();
        List<Epic> epics = loadedTasks.getEpics();

        assertEquals(1, epics.size());
        assertEquals(2, loadedTasks.getGeneratedId());
        Epic loadedEpic = loadedTasks.getEpicById(1);
        assertEquals(epic, loadedEpic);
        List<Integer> subtasksId = epic.getSubtasksIds();
        assertEquals(0, subtasksId.size());
    }

    @Test
    public void saveAndLoadEmptyHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        HttpTaskManager loadedTasks = HttpTaskManager.loadFromServer();
        List<Task> history = loadedTasks.getHistory();

        assertEquals(0, history.size());
    }
}
