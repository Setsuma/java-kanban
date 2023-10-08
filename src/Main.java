import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.createTask(new Task("Сходить в магазин", "В магазин около дома"));
        inMemoryTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу"));
        inMemoryTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        inMemoryTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3));
        inMemoryTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3));
        inMemoryTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 3));
        inMemoryTaskManager.createEpic(new Epic("Выйти погулять", "В выходные"));

        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubtaskById(5);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getEpicById(7);
        inMemoryTaskManager.getTaskById(1);

        for (Task tasks : inMemoryTaskManager.getHistory()) {
            System.out.println(tasks);
        }

        System.out.println("-------------------");
        inMemoryTaskManager.deleteTaskById(2);
        inMemoryTaskManager.deleteEpicById(3);

        for (Task tasks : inMemoryTaskManager.getHistory()) {
            System.out.println(tasks);
        }
    }
}