package task_tracker.manager;

import task_tracker.tasks.Epic;
import task_tracker.tasks.Subtask;
import task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>(); // Массив для хранения истории просмотров
    private int maxHistorySize = 10; // Переменная для хранения максимального размера истории просмотров

    public int getMaxHistorySize() {
        return maxHistorySize;
    }

    public void setMaxHistorySize(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
    }

    // Добавляем просмотренные задачи в список
    @Override
    public void add(Task task) {
        if (history.size() == maxHistorySize) {
            history.removeFirst();
        }
        history.add(copyTask(task));
    }

    // Возвращаем историю просмотров
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    // Создадим метод, который будет возвращать копию задачи, подзадачи или эпика для сохранения старой версии в истории
    private Task copyTask(Task task) {
        if (task instanceof Subtask) {
            Subtask original = (Subtask) task;
            Subtask copy = new Subtask(task.getId(), task.getName(), task.getDescription(), task.getStatus(), ((Subtask) task).getEpicId());
            return copy;
        } else if (task instanceof Epic) {
            Epic original = (Epic) task;
            Epic copy = new Epic(task.getId(), task.getName(), task.getDescription());
            copy.setStatus(original.getStatus());
            copy.setSubtaskIds(original.getSubtaskIds());
            return copy;
        } else {
            Task copy = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
            return copy;
        }
    }
}
