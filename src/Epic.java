import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    // Конструктор для создания эпика без указания айди
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    // Метод для получения списка сабтасок
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    // Метод для добавления сабтаски
    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    // Метод для обновления сабтасок внутри эпика
    public void updateSubtask(Subtask subtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (Objects.equals(subtasks.get(i).getId(), subtask.getId())) {
                subtasks.set(i, subtask);
            }
        }
    }

    // Метод для удаления сабтаски
    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

}
