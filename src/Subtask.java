public class Subtask extends Task {
    private Integer epicId;

    // Конструктор для создания сабтаски без указания айди
    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    // Конструктор для создания сабтаски с указанием айди и статуса
    public Subtask(Integer id, String name, String description, Status status, Integer epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpic(Integer epicId) {
        this.epicId = epicId;
    }
}
