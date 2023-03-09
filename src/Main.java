import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    //Извините за непонятный main, я просто тестировал функционал, поэтому не сильно следил за правильностью кода в нем.
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.createTask(new Task("Сходить в магазин", "В магазин около дома"));
        inMemoryTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу"));
        inMemoryTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        inMemoryTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3));
        inMemoryTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3));
        inMemoryTaskManager.createEpic(new Epic("Выйти погулять", "В выходные"));
        inMemoryTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 6));
        Task task = inMemoryTaskManager.getTaskById(1);
        task = inMemoryTaskManager.getTaskById(2);
        task = inMemoryTaskManager.getTaskById(3);
        Epic epic = inMemoryTaskManager.getEpicById(3);
        epic = inMemoryTaskManager.getEpicById(6);
        epic = inMemoryTaskManager.getEpicById(9);
        Subtask subtask = inMemoryTaskManager.getSubtaskById(4);
        subtask = inMemoryTaskManager.getSubtaskById(5);
        subtask = inMemoryTaskManager.getSubtaskById(7);
        subtask = inMemoryTaskManager.getSubtaskById(8);
        task = inMemoryTaskManager.getTaskById(1);
        task = inMemoryTaskManager.getTaskById(1);
        task = inMemoryTaskManager.getTaskById(1);
        task = inMemoryTaskManager.getTaskById(1);
        task = inMemoryTaskManager.getTaskById(2);
        for (Task task1 : inMemoryTaskManager.getHistory()) {
            System.out.println(task1.getId());
        }
    }
}