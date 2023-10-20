package tests;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void getHistoryWithStandardBehavior() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.of(2021, 5, 27, 18, 30), 50);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(3, history.size());
        assertEquals(task3, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task1, history.get(2));
    }

    @Test
    void getEmptyHistory() {
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void addWithStandardBehavior() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        task.setId(1);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void addInvalidTask() {
        historyManager.add(null);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void addDuplicateTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void removeWithStandardBehavior() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.of(2021, 5, 27, 18, 30), 50);
        Task task4 = new Task("Задача 4", "Описание задачи 4", LocalDateTime.of(2020, 5, 27, 18, 30), 50);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        task4.setId(4);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        historyManager.remove(task2.getId());

        final List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task4, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task1, history.get(2));
    }

    @Test
    void removeFromStartOfHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.of(2021, 5, 27, 18, 30), 50);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task3, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void removeFromMiddleOfHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.of(2021, 5, 27, 18, 30), 50);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task3, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void removeFromEndOfHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        Task task3 = new Task("Задача 3", "Описание задачи 3", LocalDateTime.of(2021, 5, 27, 18, 30), 50);
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void removeNonexistentTask() {
        historyManager.remove(-1);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void removeEpicWithSubtasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteEpicById(1);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertEquals(0, history.size());
    }
}