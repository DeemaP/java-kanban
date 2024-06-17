package task_tracker.manager;

import task_tracker.tasks.Epic;
import task_tracker.tasks.Subtask;
import task_tracker.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new LinkedList<>(); // Массив для хранения истории просмотров
    private static final int MAX_HISTORY_SIZE = 10; // Константа для хранения максимального размера истории просмотров

    // Добавляем просмотренные задачи в список
    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(copyTask(task));
    }

    // Возвращаем историю просмотров
    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }

    // Создадим метод, который будет возвращать копию задачи, подзадачи или эпика для сохранения старой версии в истории
    private Task copyTask(Task task) {
        if (task instanceof Subtask) {
            return new Subtask(task.getId(), task.getName(), task.getDescription(),
                    task.getStatus(), ((Subtask) task).getEpicId());
        } else if (task instanceof Epic) {
            Epic copy = new Epic(task.getId(), task.getName(), task.getDescription());
            copy.setStatus(task.getStatus());
            copy.setSubtaskIds(((Epic) task).getSubtaskIds());
            return copy;
        } else {
            return new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        }
    }
}
