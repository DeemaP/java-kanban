package tasktracker.manager;

import org.junit.jupiter.api.Test;
import tasktracker.enums.Status;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.util.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;

    @Override
    protected FileBackedTaskManager createTaskManager() throws IOException {
        tempFile = Files.createTempFile("test", ".tmp").toFile();
        tempFile.deleteOnExit();
        return (taskManager = (FileBackedTaskManager) Managers.getDefaultFileBackedTaskManager(tempFile.toPath()));
    }

    // Проверим сохранение пустого менеджера и загрузку из пустого файла
    @Test
    public void testSavingAndLoadingAnEmptyFile() {
        // Сохраним пустой менеджер
        taskManager.save();
        // Загрузим менеджер из пустого временного файла и проверим, что он пуст
        TaskManager loadedFileBackedTaskManager =
                FileBackedTaskManager.loadFromFile(tempFile.toPath());

        assertTrue(loadedFileBackedTaskManager.getTasks().isEmpty(), "Список задач должен быть пуст");
        assertTrue(loadedFileBackedTaskManager.getEpics().isEmpty(), "Список эпиков должен быть пуст");
        assertTrue(loadedFileBackedTaskManager.getSubtasks().isEmpty(), "Список подзадач должен быть пуст");
    }

    // Проверим сохранение и загрузку нескольких задач, эпиков и подзадач
    @Test
    void testSavingAndLoadingTasksEpicsSubtasks() {
        // Создадим несколько задач, эпиков и подзадач
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2",
                "Описание подзадачи 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3",
                "Описание подзадачи 3", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3); // Сохранение менеджера происходит автоматически при создании подзадачи

        // Создадим списки для сравнения с загруженными
        List<Task> oldTasks = taskManager.getTasks();
        List<Epic> oldEpics = taskManager.getEpics();
        List<Subtask> oldSubtasks = taskManager.getSubtasks();

        // Загрузим новый менеджер и создадим списки его задач, эпиков и подзадач
        TaskManager loadedTaskManager = Managers.getDefaultFileBackedTaskManager(tempFile.toPath());
        List<Task> newTasks = loadedTaskManager.getTasks();
        List<Epic> newEpics = loadedTaskManager.getEpics();
        List<Subtask> newSubtasks = loadedTaskManager.getSubtasks();

        // Сравним сохраненные и загруженные задачи
        for (int i = 0; i < newTasks.size(); i++) {
            assertEquals(oldTasks.get(i), newTasks.get(i), "Задачи должны быть одинаковы");
        }

        // Сравним сохраненные и загруженные эпики
        for (int i = 0; i < newEpics.size(); i++) {
            assertEquals(oldEpics.get(i), newEpics.get(i), "Эпики должны быть одинаковы");
        }

        // Сравним сохраненные и загруженные подзадачи
        for (int i = 0; i < newSubtasks.size(); i++) {
            assertEquals(oldSubtasks.get(i), newSubtasks.get(i), "Подзадачи должны быть одинаковы");
        }
    }
}
