package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds;

    // Конструктор для создания эпика без указания айди
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    // Метод для получения списка айди сабтасок
    public List<Integer> getSubtaskIds() {
        return subtaskIds;}

    // Метод для добавления сабтаски
    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }
}
