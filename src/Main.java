import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTaskManager = Managers.loadFromFile(".\\java-kanban-main\\src\\save");

//        fileBackedTaskManager.createTask(new Task("Погулять", "Во дворе"));
//        fileBackedTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу"));
//        fileBackedTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
//        fileBackedTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3));
//        fileBackedTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3));
//        fileBackedTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 3));
//        fileBackedTaskManager.createEpic(new Epic("Заняться программированием на Java", "В выходные"));
//        fileBackedTaskManager.getTaskById(1);
//        fileBackedTaskManager.getSubtaskById(5);
//        fileBackedTaskManager.getEpicById(3);
//        fileBackedTaskManager.getEpicById(7);
//        fileBackedTaskManager.getTaskById(1);

        for (Task tasks : fileBackedTaskManager.getEpics()) {
            System.out.println(tasks);
        }

        System.out.println("-------------------");
        for (Task tasks : fileBackedTaskManager.getTasks()) {
            System.out.println(tasks);
        }

        System.out.println("-------------------");
        for (Task tasks : fileBackedTaskManager.getHistory()) {
            System.out.println(tasks);
        }
    }
}
