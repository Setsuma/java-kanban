package manager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager loadFromFile(String path){
        FileBackedTasksManager fileManager = new FileBackedTasksManager(path);
        fileManager.upload();
        return fileManager;
    }
    }

