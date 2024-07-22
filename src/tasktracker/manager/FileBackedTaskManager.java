package tasktracker.manager;

import tasktracker.enums.Status;
import tasktracker.exceptions.ManagerException;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.util.CSVFormatter;
import tasktracker.util.Managers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path file;

    public FileBackedTaskManager(Path file) {
        this.file = file;
    }

    // Метод для загрузки Менеджера из статичного файла в папке /resources
    public static FileBackedTaskManager loadFromFile(Path filePath) {
        // Проверяем существование файла
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                throw ManagerException.fileCreateException(e);
            }
        }

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(filePath);

        try {
            List<String> lines = Files.readAllLines(filePath);
            int maxId = 0;

            for (int i = 1; i < lines.size(); i++) { // Итерация начинается с 1 для пропуска заголовка
                Task task = CSVFormatter.taskFromString(lines.get(i));
                if (task instanceof Epic) {
                    fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                } else {
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                }

                // Обновляем новый айди, чтобы учесть существующие айди.
                if (task.getId() > maxId) maxId = task.getId();
                fileBackedTaskManager.newId = maxId + 1;
            }

            // Добавляем подзадачи в их эпики
            for (Subtask subtask : fileBackedTaskManager.subtasks.values()) {
                Epic epic = fileBackedTaskManager.epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.addSubtask(subtask.getId());
                }
            }
        } catch (IOException e) {
            throw ManagerException.loadException(e);
        }
        return fileBackedTaskManager;
    }

    // Метод для сохранения в статичный файл
    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile()))) {
            // Запись заголовка
            bw.write(CSVFormatter.getHeader());
            bw.newLine();

            // Запись задач
            for (Task task : getTasks()) {
                bw.write(CSVFormatter.taskToString(task));
                bw.newLine();
            }

            // Запись эпиков вместе с подзадачами
            for (Epic epic : getEpics()) {
                bw.write(CSVFormatter.taskToString(epic));
                bw.newLine();
                for (int subtaskId : epic.getSubtaskIds()) {
                    Subtask subtask = subtasks.get(subtaskId);
                    if (subtask != null) {
                        bw.write(CSVFormatter.taskToString(subtask));
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw ManagerException.saveException(e);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public boolean deleteTask(int taskId) {
        boolean deleted = super.deleteTask(taskId);
        save();
        return deleted;
    }

    @Override
    public boolean deleteEpic(int epicId) {
        boolean deleted = super.deleteEpic(epicId);
        save();
        return deleted;
    }

    @Override
    public void deleteSubtask(Integer subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    @Override
    public boolean deleteAllTasks() {
        boolean deleted = super.deleteAllTasks();
        save();
        return deleted;
    }

    @Override
    public boolean deleteAllEpics() {
        boolean deleted = super.deleteAllEpics();
        save();
        return deleted;
    }

    @Override
    public boolean deleteAllSubtasks() {
        boolean deleted = super.deleteAllSubtasks();
        save();
        return deleted;
    }

    // Дополнительное задание спринт 7
    public static void main(String[] args) {
        Path filePath = Path.of("resources/file.csv");

        TaskManager oldFileBackedTaskManager = Managers.getDefaultFileBackedTaskManager(filePath);

        System.out.println("Создадим две задачи, один эпик с тремя подзадачами и эпик без подзадач.");
        System.out.println("~~~".repeat(10));
        System.out.println();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        oldFileBackedTaskManager.createTask(task1);
        oldFileBackedTaskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        oldFileBackedTaskManager.createEpic(epic1);
        oldFileBackedTaskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1",
                "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2",
                "Описание подзадачи 2", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3",
                "Описание подзадачи 3", Status.NEW, epic1.getId());
        oldFileBackedTaskManager.createSubtask(subtask1);
        oldFileBackedTaskManager.createSubtask(subtask2);
        oldFileBackedTaskManager.createSubtask(subtask3);

        System.out.println("Создадим списки для сравнения");
        List<Task> oldTasks = oldFileBackedTaskManager.getTasks();
        List<Epic> oldEpics = oldFileBackedTaskManager.getEpics();
        List<Subtask> oldSubtasks = oldFileBackedTaskManager.getSubtasks();

        System.out.println("Создадим новый менеджер из нашего файла");
        System.out.println("~~~".repeat(10));
        System.out.println();

        TaskManager newFileBackedTaskManager = Managers.getDefaultFileBackedTaskManager(filePath);

        System.out.println("Создадим новые списки задач, эпиков и подзадач, загруженные из файла" +
                " и убедимся, что новые и старые списки одинаковы");
        List<Task> newTasks = newFileBackedTaskManager.getTasks();
        List<Epic> newEpics = newFileBackedTaskManager.getEpics();
        List<Subtask> newSubtasks = newFileBackedTaskManager.getSubtasks();

        for (int i = 0; i < newTasks.size(); i++) {
            System.out.println("Задача " + '\'' + oldTasks.get(i).getName() + '\'' +
                    " есть в старом и новом списках: " + newTasks.get(i).equals(oldTasks.get(i)));
        }

        for (int i = 0; i < newEpics.size(); i++) {
            System.out.println("Эпик " + '\'' + oldEpics.get(i).getName() + '\'' +
                    " есть в старом и новом списках: " + newEpics.get(i).equals(oldEpics.get(i)));
        }

        for (int i = 0; i < newSubtasks.size(); i++) {
            System.out.println("Подзадача " + '\'' + oldSubtasks.get(i).getName() + '\'' +
                    " есть в старом и новом списках: " + newSubtasks.get(i).equals(oldSubtasks.get(i)));
        }

        // Очистим наш файл, чтобы при перезапуске теста он был пуст.
        newFileBackedTaskManager.deleteAllTasks();
        newFileBackedTaskManager.deleteAllEpics();
    }
}
