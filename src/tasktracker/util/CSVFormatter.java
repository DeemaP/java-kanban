package tasktracker.util;

import tasktracker.enums.Status;
import tasktracker.enums.TaskTypes;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;

public class CSVFormatter {

    // Приватный конструктор, чтобы нельзя было создать экземпляр утилитарного класса
    private CSVFormatter() {
    }

    public static String taskToString(Task task) {
        if (task instanceof Epic) {
            return task.getId() + "," +
                    TaskTypes.EPIC + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + ",";
            // Принадлежность к эпику есть только у подзадачи, не добавляем epicId
        } else if (task instanceof Subtask) {
            return task.getId() + "," +
                    TaskTypes.SUBTASK + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + "," +
                    ((Subtask) task).getEpicId();
        } else {
            return task.getId() + "," +
                    TaskTypes.TASK + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + ",";
            // Принадлежность к эпику есть только у подзадачи, не добавляем epicId
        }
    }

    public static Task taskFromString(String taskInString) {
        String[] fields = taskInString.split(",");

        int id = Integer.parseInt(fields[0]);
        TaskTypes type = TaskTypes.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case EPIC:
                return new Epic(id, name, description);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]); // При возвращении подзадачи добавляем epicId
                return new Subtask(id, name, description, status, epicId);
            default:
                return new Task(id, name, description, status);
        }
    }

    public static String getHeader() {
        return "id, type, name, status, description, epic";
    }
}
