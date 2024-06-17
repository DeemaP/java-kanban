package task_tracker.util;

import task_tracker.manager.HistoryManager;
import task_tracker.manager.InMemoryHistoryManager;
import task_tracker.manager.InMemoryTaskManager;
import task_tracker.manager.TaskManager;

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