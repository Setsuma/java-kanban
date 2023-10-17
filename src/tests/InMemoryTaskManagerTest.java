package tests;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void generateTasks() {
        epic = new Epic("Эпик 1", "Описание эпика 1");
        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 10), 10);
        subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 10);
    }

    @Test
    public void calculateStatusEmptySubtaskList() {
        taskManager.createEpic(epic);

        Status status = taskManager.getEpicById(1).getStatus();
        assertEquals(Status.NEW, status);
    }

    @Test
    public void calculateStatusAllSubtasksNew() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Status status = taskManager.getEpicById(epic.getId()).getStatus();
        assertEquals(Status.NEW, status);
    }

    @Test
    public void calculateStatusAllSubtasksDone() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Status status = taskManager.getEpicById(epic.getId()).getStatus();
        assertEquals(Status.DONE, status);
    }

    @Test
    public void calculateStatusMixedSubtaskStatuses() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Status status = taskManager.getEpicById(epic.getId()).getStatus();
        assertEquals(Status.IN_PROGRESS, status);
    }

    @Test
    public void calculateStatusAllSubtasksInProgress() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        Status status = taskManager.getEpicById(epic.getId()).getStatus();
        assertEquals(Status.IN_PROGRESS, status);
    }

    @Test
    public void calculateEpicDurationWithSeveralSubtasks() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        long epicDuration = epic.getDuration().toMinutes();
        assertEquals(20, epicDuration);
    }

    @Test
    public void calculateEpicDurationWithOneSubtask() {
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);

        long epicDuration = epic.getDuration().toMinutes();
        assertEquals(10, epicDuration);
    }

    @Test
    public void getPrioritizedTasks() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 1, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createTask(task);
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size());
        assertEquals(taskManager.getTaskById(4), prioritizedTasks.get(0));
        assertEquals(taskManager.getSubtaskById(2), prioritizedTasks.get(1));
        assertEquals(taskManager.getSubtaskById(3), prioritizedTasks.get(2));
    }

    @Test
    public void getEmptyPrioritizedTasks() {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0, prioritizedTasks.size());
    }
}