package task_tracker.manager;

import task_tracker.tasks.Epic;
import task_tracker.tasks.Subtask;
import task_tracker.tasks.Task;

import java.util.List;

public interface TaskManager {
    // Методы получения тасок, эпиков и сабтасок списком
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    // Методы создания тасок, эпиков и сабтасок
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    // Методы обновления тасок, эпиков и сабтасок
    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    // Методы для удаления тасок, эпиков и сабтасок
    boolean deleteTask(int taskId);

    boolean deleteEpic(int epicId);

    boolean deleteSubtask(Integer subtaskId);

    // Методы для удаления всех задач по типам
    boolean deleteAllTasks();

    boolean deleteAllEpics();

    boolean deleteAllSubtasks();

    // Дополнительный метод для вывода подзадач по эпику
    List<Subtask> getSubtasksByEpic(int epicId);

    // Геттеры задач, эпиков и подзадач
    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);
}
