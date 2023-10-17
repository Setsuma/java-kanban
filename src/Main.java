import manager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        final String PATH = ".\\save";
        FileBackedTasksManager fileBackedTaskManager = new FileBackedTasksManager(PATH);

        fileBackedTaskManager.createTask(new Task("Погулять", "Во дворе", LocalDateTime.of(2022, 5, 27, 12, 30), 40));
        fileBackedTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу", LocalDateTime.now(), 40));
        fileBackedTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        fileBackedTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3, LocalDateTime.of(2027, 5, 27, 12, 30), 40));
        fileBackedTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3, LocalDateTime.of(2028, 5, 27, 12, 30), 700));
        fileBackedTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 3, LocalDateTime.of(2029, 5, 27, 12, 30), 40));
        fileBackedTaskManager.createEpic(new Epic("Заняться программированием на Java", "В выходные"));

        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getSubtaskById(5);
        fileBackedTaskManager.getEpicById(3);
        fileBackedTaskManager.getEpicById(7);
        fileBackedTaskManager.getTaskById(1);

        fileBackedTaskManager.createTask(new Task("Купить еды", "В магазине", LocalDateTime.now(), 40));

        System.out.println("Tasks:");
        for (Task tasks : fileBackedTaskManager.getTasks()) {
            System.out.println(tasks);
        }
        for (Task tasks : fileBackedTaskManager.getEpics()) {
            System.out.println(tasks);
        }
        for (Task tasks : fileBackedTaskManager.getSubtasks()) {
            System.out.println(tasks);
        }
        System.out.println("History:");
        for (Task tasks : fileBackedTaskManager.getHistory()) {
            System.out.println(tasks);
        }

        System.out.println("\n-------------------\n");
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(PATH);

        System.out.println("Tasks:");
        for (Task tasks : newManager.getTasks()) {
            System.out.println(tasks);
        }
        for (Task tasks : newManager.getEpics()) {
            System.out.println(tasks);
        }
        for (Task tasks : newManager.getSubtasks()) {
            System.out.println(tasks);
        }
        System.out.println("History:");
        for (Task tasks : newManager.getHistory()) {
            System.out.println(tasks);
        }

        System.out.println("\n-------------------\n");
        for (Task task : fileBackedTaskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        System.out.println("\n~~~~~~~~~~~~~~~~~~~\n");

        for (Task task : newManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}
