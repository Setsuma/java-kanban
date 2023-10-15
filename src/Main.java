import manager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        final String PATH = ".\\save";
        FileBackedTasksManager fileBackedTaskManager = new FileBackedTasksManager(PATH);

        fileBackedTaskManager.createTask(new Task("Погулять", "Во дворе"));
        fileBackedTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу"));
        fileBackedTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        fileBackedTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3));
        fileBackedTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3));
        fileBackedTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 3));
        fileBackedTaskManager.createEpic(new Epic("Заняться программированием на Java", "В выходные"));

        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getSubtaskById(5);
        fileBackedTaskManager.getEpicById(3);
        fileBackedTaskManager.getEpicById(7);
        fileBackedTaskManager.getTaskById(1);

        fileBackedTaskManager.deleteTaskById(2);
        fileBackedTaskManager.createTask(new Task("Купить еды", "В магазине"));

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
    }
}
