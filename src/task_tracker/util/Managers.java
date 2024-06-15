package task_tracker.util;

import task_tracker.manager.HistoryManager;
import task_tracker.manager.InMemoryHistoryManager;
import task_tracker.manager.InMemoryTaskManager;
import task_tracker.manager.TaskManager;

public class Managers {
    private static final HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

}