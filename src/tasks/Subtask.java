package tasks;

public class Subtask extends Task {
    private int epicId;

    // Конструктор для создания сабтаски без указания айди
    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    // Конструктор для создания сабтаски с указанием айди и статуса
    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpic(int epicId) {
        this.epicId = epicId;
    }
}
