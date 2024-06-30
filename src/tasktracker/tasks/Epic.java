package tasktracker.tasks;

import tasktracker.enums.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subtaskIds;

    // Конструктор для создания эпика без указания айди
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    // Конструктор для создания эпика с указанием айди
    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    // Метод для получения списка айди сабтасок
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    // Метод для добавления сабтаски
    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }
}
