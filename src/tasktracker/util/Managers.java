package tasktracker.util;

import tasktracker.manager.FileBackedTaskManager;
import tasktracker.manager.HistoryManager;
import tasktracker.manager.InMemoryHistoryManager;
import tasktracker.manager.InMemoryTaskManager;
import tasktracker.manager.TaskManager;

import java.nio.file.Path;

public class Managers {

    // Приватный конструктор, чтобы нельзя было создать экземпляр утилитарного класса
    private Managers() {
    }

    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultInMemoryHistoryManager() {
        return new InMemoryHistoryManager();
    }

    // Метод для загрузки FileBackedTaskManager из нашего статичного системного файла
    public static TaskManager getDefaultFileBackedTaskManager() {
        return FileBackedTaskManager.loadFromFile();
    }

    // Метод для создания FileBackedTaskManager из конкретного файла (для тестов)
    public static TaskManager getDefaultFileBackedTaskManager(Path path) {
        return new FileBackedTaskManager(path);
    }
}