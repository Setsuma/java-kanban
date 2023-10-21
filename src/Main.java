import manager.HttpTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpTaskManager httpTaskManager = Managers.getDefault();
        httpTaskManager.createTask(new Task("Погулять", "Во дворе", LocalDateTime.of(2022, 5, 27, 12, 30), 40));
        httpTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу", LocalDateTime.now(), 40));
        httpTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        httpTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3, LocalDateTime.of(2027, 5, 27, 12, 30), 40));
        httpTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3, LocalDateTime.of(2028, 5, 27, 12, 30), 700));
        httpTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 3, LocalDateTime.of(2029, 5, 27, 12, 30), 40));
        httpTaskManager.createEpic(new Epic("Заняться программированием на Java", "В выходные"));

        httpTaskManager.getTaskById(1);
        httpTaskManager.getSubtaskById(5);
        httpTaskManager.getEpicById(3);
        httpTaskManager.getEpicById(7);
        httpTaskManager.getTaskById(1);

        httpTaskManager.createTask(new Task("Купить еды", "В магазине", LocalDateTime.now(), 40));

        System.out.println("Tasks:");
        for (Task tasks : httpTaskManager.getTasks()) {
            System.out.println(tasks);
        }
        for (Task tasks : httpTaskManager.getEpics()) {
            System.out.println(tasks);
        }
        for (Task tasks : httpTaskManager.getSubtasks()) {
            System.out.println(tasks);
        }
        System.out.println("History:");
        for (Task tasks : httpTaskManager.getHistory()) {
            System.out.println(tasks);
        }

        System.out.println("\n-------------------\n");
        HttpTaskManager newManager = HttpTaskManager.loadFromServer();

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
        for (Task task : httpTaskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        System.out.println("\n~~~~~~~~~~~~~~~~~~~\n");

        for (Task task : newManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}
