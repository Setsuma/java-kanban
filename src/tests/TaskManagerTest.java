package tests;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void beforeEach() {
        taskManager = createTaskManager();
    }

    @Test
    void getTasksWithStandardBehavior() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void getTasksWithEmptyList() {
        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertEquals(0, tasks.size());
    }

    @Test
    void getTaskWithInvalidTaskId() {
        Task task = taskManager.getTaskById(-1);

        assertNull(task);
    }

    @Test
    void getSubtasksWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void getSubtasksWithEmptyList() {
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks);
        assertEquals(0, subtasks.size());
    }

    @Test
    void getSubtaskWithInvalidSubtaskId() {
        Subtask subtask = taskManager.getSubtaskById(-1);

        assertNull(subtask);
    }

    @Test
    void getEpicsWithStandardBehavior() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Выбросить мусор", "Быстро");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 1", "Описание подзадачи 1", 2, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics);
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void getSEpicsWithEmptyList() {
        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics);
        assertEquals(0, epics.size());
    }

    @Test
    void getEpicWithInvalidEpicId() {
        Epic epic = taskManager.getEpicById(-1);

        assertNull(epic);
    }

    @Test
    void clearTasksWithStandardBehavior() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.clearTasks();

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void clearTasksWithEmptyList() {
        taskManager.clearTasks();

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void clearEpicsWithStandardBehavior() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Выбросить мусор", "Быстро");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 2, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.clearEpics();

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics);
        assertTrue(epics.isEmpty());

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void clearEpicsWithEmptyList() {
        taskManager.clearEpics();

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics);
        assertTrue(epics.isEmpty());

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void clearSubtasksWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.clearSubtasks();

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks);
        assertEquals(0, subtasks.size());
    }

    @Test
    void clearSubtasksWithEmptyTaskList() {
        taskManager.clearSubtasks();

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks);
        assertEquals(0, subtasks.size());
    }

    @Test
    void getTaskByIdWithStandardBehavior() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTaskById(task.getId());

        assertEquals(task, retrievedTask);
    }

    @Test
    void getTaskByIdWithEmptyTaskList() {
        Task retrievedTask = taskManager.getTaskById(1);

        assertNull(retrievedTask);
    }

    @Test
    void getTaskByIdWithInvalidTaskId() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTaskById(-1);

        assertNull(retrievedTask);
    }

    @Test
    void getEpicByIdWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Task retrievedEpic = taskManager.getEpicById(epic.getId());

        assertEquals(epic, retrievedEpic);
    }

    @Test
    void getEpicByIdWithEmptyTaskList() {
        Epic retrievedEpic = taskManager.getEpicById(1);

        assertNull(retrievedEpic);
    }

    @Test
    void getEpicByIdWithInvalidEpicId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Task retrievedEpic = taskManager.getEpicById(-1);

        assertNull(retrievedEpic);
    }

    @Test
    void getSubtaskByIdWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtaskById(subtask.getId());

        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    void getSubtaskByIdWithEmptyTaskList() {
        Subtask retrievedSubtask = taskManager.getSubtaskById(1);

        assertNull(retrievedSubtask);
    }

    @Test
    void getSubtaskByIdWithInvalidEpicId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtaskById(-1);

        assertNull(retrievedSubtask);
    }

    @Test
    void createTaskWithStandardBehavior() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);

        taskManager.createTask(task);

        assertTrue(taskManager.getTasks().contains(task));
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void createTaskWithIntersectingTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2023, 5, 27, 18, 35), 40);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, LocalDateTime.of(2023, 5, 27, 18, 38), 30);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        assertTrue(taskManager.getTasks().contains(task1));
        assertFalse(taskManager.getTasks().contains(task2));
        assertFalse(taskManager.getSubtasks().contains(subtask));
        assertEquals(Status.NEW, task1.getStatus());
    }

    @Test
    void createTaskWithNullTask() {
        taskManager.createTask(null);

        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void createEpicWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertTrue(taskManager.getEpics().contains(epic));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void createEpicWithNullEpic() {
        taskManager.createEpic(null);

        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void createSubtaskWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        assertTrue(taskManager.getSubtasks().contains(subtask));
        assertEquals(Status.NEW, subtask.getStatus());
    }

    @Test
    void createSubtaskWithIntersectingTasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 50), 50);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createTask(task);

        assertTrue(taskManager.getSubtasks().contains(subtask1));
        assertFalse(taskManager.getSubtasks().contains(subtask2));
        assertFalse(taskManager.getTasks().contains(task));
        assertEquals(Status.NEW, subtask1.getStatus());
    }

    @Test
    void createSubtaskWithNullSubtask() {
        taskManager.createSubtask(null);

        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void updateTaskWithStandardBehavior() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 19, 10), 60);
        taskManager.createTask(task);

        Task updatedTask = new Task("Задача 1.2", "Поменяли описание", LocalDateTime.of(2023, 5, 27, 18, 50), 50);
        updatedTask.setId(1);
        updatedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(updatedTask);

        assertEquals(updatedTask, taskManager.getTaskById(1));
    }

    @Test
    public void updateTaskWithEmptyTaskList() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 19, 10), 60);
        task.setId(1);
        task.setStatus(Status.NEW);
        taskManager.updateTask(task);

        assertNull(taskManager.getTaskById(1));
    }

    @Test
    public void updateTaskWithNonexistentTaskId() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 19, 10), 60);
        taskManager.createTask(task);

        Task updatedTask = new Task("Задача 1.2", "Поменяли описание", LocalDateTime.of(2023, 5, 27, 18, 50), 50);
        updatedTask.setId(2);
        updatedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(updatedTask);

        assertNotEquals(updatedTask, taskManager.getTaskById(1));
    }

    @Test
    public void updateTaskWithIntersectingTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 19, 10), 60);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 19, 10), 60);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Task updatedTask = new Task("Задача 1.2", "Измененное описание задачи 1", LocalDateTime.of(2022, 5, 27, 18, 50), 50);
        updatedTask.setId(1);
        updatedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(updatedTask);

        assertNotEquals(updatedTask, taskManager.getTaskById(1));
    }

    @Test
    public void updateEpicStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);

        Epic updatedEpic = new Epic("Эпик 1.2", "Измененное Описание эпика 1");
        updatedEpic.setId(1);
        updatedEpic.setStatus(Status.NEW);
        taskManager.updateEpic(updatedEpic);

        assertEquals(updatedEpic.toString(), taskManager.getEpicById(1).toString());
    }

    @Test
    public void updateEpicWithEmptyEpicList() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");

        Epic updatedEpic = new Epic("Эпик 1.2", "Измененное описание эпика 1");
        updatedEpic.setId(1);
        updatedEpic.setStatus(Status.NEW);
        taskManager.deleteEpicById(1);

        taskManager.updateEpic(epic);

        assertNull(taskManager.getEpicById(1));
    }

    @Test
    public void updateEpicWithNonexistentEpicId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);

        Epic updatedEpic = new Epic("Эпик 1.2", "Измененное описание эпика 1");
        updatedEpic.setId(2);
        updatedEpic.setStatus(Status.NEW);

        taskManager.updateEpic(updatedEpic);

        assertNotEquals(updatedEpic.toString(), taskManager.getEpicById(1).toString());
    }

    @Test
    public void updateSubtaskStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Subtask updatedSubtask = new Subtask("Подзадача 1.2", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 60);
        updatedSubtask.setId(2);
        updatedSubtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(updatedSubtask);

        assertEquals(updatedSubtask, taskManager.getSubtaskById(2));
    }

    @Test
    public void updateSubtaskWithEmptySubtaskList() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        subtask.setId(1);
        subtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask);

        assertNull(taskManager.getSubtaskById(1));
    }

    @Test
    public void updateSubtaskWithNonexistentSubtaskId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Subtask updatedSubtask = new Subtask("Подзадача 1.2", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 60);
        updatedSubtask.setId(3);
        updatedSubtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(updatedSubtask);

        assertNotEquals(updatedSubtask, taskManager.getSubtaskById(2));
    }

    @Test
    public void updateSubtaskWithNonexistentEpicId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        Subtask updatedSubtask = new Subtask("Подзадача 1.2", "Описание подзадачи 1", 3, LocalDateTime.of(2023, 5, 27, 18, 30), 60);
        updatedSubtask.setId(3);
        updatedSubtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(updatedSubtask);

        assertNotEquals(updatedSubtask, taskManager.getSubtaskById(2));
    }

    @Test
    public void updateSubtaskWithIntersectingTasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        Subtask subtask2 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2022, 5, 27, 18, 38), 30);

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Subtask updatedSubtask = new Subtask("Подзадача 1.2", "Измененное описание подзадачи 1", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 30);
        updatedSubtask.setId(2);
        updatedSubtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(updatedSubtask);

        assertNotEquals(updatedSubtask, taskManager.getSubtaskById(2));
    }

    @Test
    public void deleteTaskByIdWithStandardBehavior() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 19, 10), 60);
        taskManager.createTask(task);
        taskManager.deleteTaskById(1);

        assertNull(taskManager.getTaskById(1));
    }

    @Test
    public void deleteTaskByIdWithEmptyTaskList() {
        taskManager.deleteTaskById(1);

        assertNull(taskManager.getTaskById(1));
    }

    @Test
    public void deleteTaskByIdWithNonexistentTaskId() {
        Task task = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 19, 10), 60);
        taskManager.createTask(task);
        taskManager.deleteTaskById(2);

        assertNotNull(taskManager.getTaskById(1));
    }

    @Test
    public void deleteEpicByIdStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.deleteEpicById(1);
        taskManager.deleteSubtaskById(2);

        assertNull(taskManager.getEpicById(1));
        assertNull(taskManager.getSubtaskById(2));
    }

    @Test
    public void deleteEpicByIdWithEmptyEpicList() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        epic.setId(1);
        taskManager.deleteEpicById(1);

        assertNull(taskManager.getEpicById(1));
    }

    @Test
    public void deleteEpicByIdWithNonexistentEpicId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        taskManager.deleteEpicById(2);

        assertNotNull(taskManager.getEpicById(1));
    }

    @Test
    public void deleteSubtaskByIdWithStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtaskById(2);

        assertNotNull(taskManager.getEpicById(1));
        assertNull(taskManager.getSubtaskById(2));
        assertEquals(0, taskManager.getEpicById(1).getSubtasksIds().size());
    }

    @Test
    public void deleteSubtaskByIdWithEmptySubtaskList() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        subtask.setId(2);
        taskManager.createEpic(epic);
        taskManager.deleteSubtaskById(2);

        assertNull(taskManager.getSubtaskById(2));
    }

    @Test
    public void deleteSubtaskByIdWithNonexistentSubtaskId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 38), 30);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.deleteSubtaskById(3);

        assertNotNull(taskManager.getEpicById(1));
        assertNotNull(taskManager.getSubtaskById(2));
        assertEquals(1, taskManager.getEpicById(1).getSubtasksIds().size());
    }

    @Test
    public void getSubtasksByEpicIdStandardBehavior() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(1);

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    public void getSubtasksByEpicIdWithEmptySubtaskList() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(1);

        assertEquals(0, subtasks.size());
    }

    @Test
    public void getSubtasksByEpicIdWithNonexistentEpicId() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1, LocalDateTime.of(2023, 5, 27, 18, 30), 30);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 1, LocalDateTime.of(2022, 5, 27, 18, 30), 50);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(5);

        assertNull(subtasks);
    }

    @Test
    public void getHistoryWithEmptyTaskList() {
        List<Task> history = taskManager.getHistory();

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    public void getHistoryWithStandardBehavior() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", LocalDateTime.of(2023, 5, 27, 18, 30), 50);
        Task task2 = new Task("Задача 2", "Описание задачи 2", LocalDateTime.of(2022, 5, 27, 18, 30), 40);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        List<Task> history = taskManager.getHistory();

        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }
}


