package tasktracker.util;

import tasktracker.manager.HistoryManager;
import tasktracker.manager.InMemoryHistoryManager;
import tasktracker.manager.InMemoryTaskManager;
import tasktracker.manager.TaskManager;

public class Managers {
    private Managers() {
        // Приватный конструктор, чтобы нельзя было создать экземпляр утилитарного класса
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}