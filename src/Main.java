public class Main {
    public static TaskManager taskManager;

    public static void main(String[] args) {
        taskManager = new TaskManager();
        taskManager.createTask(new Task("Сходить в магазин", "В магазин около дома"));
        taskManager.createTask(new Task("Сходить на тренировку", "По волейболу"));
        taskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        taskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3));
        taskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3));
        taskManager.createEpic(new Epic("Выйти погулять", "В выходные"));
        taskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 6));
        printAllTasks();
        Task taskUpdate = new Task("Я дома", "сходил в магазин");
        taskUpdate.status = Status.DONE;
        taskUpdate.id = 1;
        taskManager.updateTask(taskUpdate);
        Subtask subtaskUpdate = new Subtask("Помыл полы", "Все чисто", 3);
        subtaskUpdate.id = 4;
        subtaskUpdate.status = Status.DONE;
        taskManager.updateSubtask(subtaskUpdate);
        printAllTasks();
        taskManager.deleteTaskById(8);
        taskManager.deleteEpicById(3);
        printAllTasks();
    }

    public static void printAllTasks() {
        System.out.println("Tasks:");
        for (Task task : taskManager.getTasks()) {
            System.out.print(task.id + " | ");
            System.out.print(task.name + " | ");
            System.out.print(task.description + " | ");
            System.out.print(task.status + "\n");
        }
        System.out.println("Epics:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.print(epic.id + " | ");
            System.out.print(epic.name + " | ");
            System.out.print(epic.description + " | ");
            System.out.print(epic.status + "\n");
        }
        System.out.println("Subtasks:");
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.print(subtask.id + " | ");
            System.out.print(subtask.name + " | ");
            System.out.print(subtask.description + " | ");
            System.out.print(subtask.status + "\n");
        }
        System.out.println("\n");
    }
}
