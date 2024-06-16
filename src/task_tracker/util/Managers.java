package task_tracker.util;

import task_tracker.manager.HistoryManager;
import task_tracker.manager.InMemoryHistoryManager;
import task_tracker.manager.InMemoryTaskManager;
import task_tracker.manager.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}