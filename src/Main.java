public class Main {
    public static InMemoryTaskManager inMemoryTaskManager;

    public static void main(String[] args) {
        inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createTask(new Task("Сходить в магазин", "В магазин около дома"));
        inMemoryTaskManager.createTask(new Task("Сходить на тренировку", "По волейболу"));
        inMemoryTaskManager.createEpic(new Epic("Убраться дома", "Завтра"));
        inMemoryTaskManager.createSubtask(new Subtask("Помыть полы", "В комнате", 3));
        inMemoryTaskManager.createSubtask(new Subtask("Протереть стол", "В комнате", 3));
        inMemoryTaskManager.createEpic(new Epic("Выйти погулять", "В выходные"));
        inMemoryTaskManager.createSubtask(new Subtask("Пригласить друзей", "Позвонить им", 6));
    /*    printAllTasks();
        Task taskUpdate = new Task("Я дома", "сходил в магазин");
        taskUpdate.status = Status.DONE;
        taskUpdate.id = 1;
        inMemoryTaskManager.updateTask(taskUpdate);
        Subtask subtaskUpdate = new Subtask("Помыл полы", "Все чисто", 3);
        subtaskUpdate.id = 4;
        subtaskUpdate.status = Status.DONE;
        inMemoryTaskManager.updateSubtask(subtaskUpdate);
        printAllTasks();
        inMemoryTaskManager.deleteTaskById(8);
        inMemoryTaskManager.deleteEpicById(3);
        Epic updateEpic = inMemoryTaskManager.getEpicById(6);
        updateEpic.setName("Погулять с друзьями");
        inMemoryTaskManager.updateEpic(updateEpic);
        printAllTasks();*/
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

    public static void printAllTasks() {
        System.out.println("Tasks:");
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.print(task.id + " | ");
            System.out.print(task.name + " | ");
            System.out.print(task.description + " | ");
            System.out.print(task.status + "\n");
        }
        System.out.println("Epics:");
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.print(epic.id + " | ");
            System.out.print(epic.name + " | ");
            System.out.print(epic.description + " | ");
            System.out.print(epic.status + "\n");
        }
        System.out.println("Subtasks:");
        for (Subtask subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.print(subtask.id + " | ");
            System.out.print(subtask.name + " | ");
            System.out.print(subtask.description + " | ");
            System.out.print(subtask.status + "\n");
        }
        System.out.println("\n");
    }
}
