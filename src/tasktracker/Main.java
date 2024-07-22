package tasktracker;

import tasktracker.enums.Status;
import tasktracker.manager.InMemoryTaskManager;
import tasktracker.manager.TaskManager;
import tasktracker.tasks.Epic;
import tasktracker.tasks.Subtask;
import tasktracker.tasks.Task;
import tasktracker.util.Managers;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        // Дополнительное задание спринт 6
        TaskManager taskManager = Managers.getDefaultInMemoryTaskManager();

        System.out.println("Создадим две задачи, один эпик с тремя подзадачами и эпик без подзадач.");
        System.out.println("~~~".repeat(10));
        System.out.println();

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
        taskManager.createSubtask(subtask3);


        System.out.println("Запросим разные задачи, эпики и подзадачи");
        System.out.println(taskManager.getTask(task1.getId()));
        System.out.println(taskManager.getEpic(epic2.getId()));
        System.out.println(taskManager.getSubtask(subtask3.getId()));
        System.out.println(taskManager.getEpic(epic2.getId()));
        System.out.println(taskManager.getSubtask(subtask1.getId()));
        System.out.println(taskManager.getSubtask(subtask3.getId()));
        System.out.println("---".repeat(10));
        System.out.println();

        System.out.println("Запросим историю");
        ArrayList<Task> history = (ArrayList<Task>) ((InMemoryTaskManager) taskManager).getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("---".repeat(10));
        System.out.println();

        System.out.println("Удалим задачу, которая есть в истории.");
        System.out.println();
        System.out.println("Удаление задачи 1: " + taskManager.deleteTask(task1.getId()));
        System.out.println();
        System.out.println("Выведем историю и проверим, что Задачи 1 в ней нет.");
        System.out.println();
        history = (ArrayList<Task>) ((InMemoryTaskManager) taskManager).getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("---".repeat(10));
        System.out.println();

        System.out.println("Запросим вывод эпика 1 и трех его подзадач");
        System.out.println(taskManager.getEpic(epic1.getId()));
        System.out.println(taskManager.getSubtask(subtask1.getId()));
        System.out.println(taskManager.getSubtask(subtask2.getId()));
        System.out.println(taskManager.getSubtask(subtask3.getId()));
        System.out.println();
        System.out.println("Запросим вывод истории:");
        history = (ArrayList<Task>) ((InMemoryTaskManager) taskManager).getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("---".repeat(10));
        System.out.println();

        System.out.println("Удалим Эпик 1 и проверим, что он удалился из истории вместе с подзадачами");
        System.out.println();
        System.out.println(taskManager.deleteEpic(epic1.getId()));
        System.out.println();
        System.out.println("Запросим вывод истории:");
        history = (ArrayList<Task>) ((InMemoryTaskManager) taskManager).getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("---".repeat(10));
        System.out.println();
        System.out.println("~~~".repeat(10));
        System.out.println("Условия дополнительного задания выполнены");
        System.out.println("~~~".repeat(10));
    }
}

