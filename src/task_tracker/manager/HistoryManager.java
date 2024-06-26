package task_tracker.manager;

import task_tracker.tasks.Task;

import java.util.List;

public interface HistoryManager {
    // Методы для управления историей просмотров
    void add(Task task);

    List<Task> getHistory();
}
