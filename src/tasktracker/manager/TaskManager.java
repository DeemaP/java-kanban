package tasktracker.manager;

import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

import java.util.List;

public interface TaskManager {
    // Методы получения тасок, эпиков и сабтасок списком
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    // Методы создания тасок, эпиков и сабтасок
    void createTask(Task task);

    void createEpic(Epic epic);

    default Subtask createSubtask(Subtask subtask) {
        return null;
    }

    // Методы обновления тасок, эпиков и сабтасок
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // Методы для удаления тасок, эпиков и сабтасок
    boolean deleteTask(int taskId);

    boolean deleteEpic(int epicId);

    void deleteSubtask(Integer subtaskId);

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
