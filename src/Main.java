import java.util.List;

public class Main {

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        TaskManager taskManager = new TaskManager();

        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        System.out.println("Тест 1");
        System.out.println("Создаем две задачи, эпик с двумя подзадачами и эпик с одной подзадачей.");
        System.out.println("~".repeat(30));

        Task task1 = new Task("задача 1", "описание задачи 1", Status.NEW);
        Task task1Created = taskManager.createTask(task1);
        Task task2 = new Task("задача 2", "описание задачи 2", Status.NEW);
        Task task2Created = taskManager.createTask(task2);

        Epic epic1 = new Epic("эпик 1", "описание эпика 1");
        Epic epic1Created = taskManager.createEpic(epic1);
        Subtask subtask11 = new Subtask("подзадача 1-1",
                "описание подзадачи 1-1", Status.NEW, epic1Created.getId());
        Subtask subtask11Created = taskManager.createSubtask(subtask11);
        Subtask subtask12 = new Subtask("подзадача 1-2",
                "описание подзадачи 1-2", Status.NEW, epic1Created.getId());
        Subtask subtask12Created = taskManager.createSubtask(subtask12);

        Epic epic2 = new Epic("эпик 2", "описание эпика 2");
        Epic epic2Created = taskManager.createEpic(epic2);
        Subtask subtask21 = new Subtask("подзадача 2-1",
                "описание подзадачи 2-1", Status.NEW, epic2Created.getId());
        Subtask subtask21Created = taskManager.createSubtask(subtask21);

        System.out.println("Задачи: ");
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(30));
        System.out.println("Эпики: ");
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(30));
        System.out.println("Подзадачи: ");
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(30));
        System.out.println();
        System.out.println();

        System.out.println("Тест 2");
        System.out.println("Меняем статусы задач и подзадач, проверяя, что статус эпика изменился вслед за подзадачей");
        System.out.println("Имена задач и описания заменены на капс");
        System.out.println("~".repeat(30));

        Task task1UpdateData = new Task(task1.getId(), "ЗАДАЧА 1",
                "ОПИСАНИЕ ЗАДАЧИ 1", Status.IN_PROGRESS);
        Task task1Updated = taskManager.updateTask(task1UpdateData);
        Task task2UpdateData = new Task(task2.getId(), "ЗАДАЧА 2",
                "ОПИСАНИЕ ЗАДАЧИ 2", Status.DONE);
        Task task2Updated = taskManager.updateTask(task2UpdateData);

        Subtask subtask11UpdateData = new Subtask(subtask11.getId(), "ПОДЗАДАЧА 1-1",
                "ОПИСАНИЕ ПОДЗАДАЧИ 1-1", Status.IN_PROGRESS, subtask11.getEpicId());
        Subtask subtask11Updated = taskManager.updateSubtask(subtask11UpdateData);
        Subtask subtask12UpdateData = new Subtask(subtask12.getId(), "ПОДЗАДАЧА 1-2",
                "ОПИСАНИЕ ПОДЗАДАЧИ 1-2", Status.DONE, subtask12.getEpicId());
        Subtask subtask12Updated = taskManager.updateSubtask(subtask12UpdateData);
        Subtask subtask21UpdateData = new Subtask(subtask21.getId(), "ПОДЗАДАЧА 2-1",
                "ОПИСАНИЕ ПОДЗАДАЧИ 2-1", Status.DONE, subtask21.getEpicId());
        Subtask subtask21Updated = taskManager.updateSubtask(subtask21UpdateData);

        System.out.println("Задачи: ");
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(30));
        System.out.println("Эпики: ");
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(30));
        System.out.println("Подзадачи: ");
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(30));
        System.out.println();
        System.out.println();

        System.out.println("Тест 3");
        System.out.println("Удаляем одну задачу, один эпик (с одной подзадачей)," +
                " одну подзадачу у эпика с двумя подзадачами");
        System.out.println("Выводим задачи, эпики и подзадачи");
        System.out.println("~".repeat(30));

        System.out.println("Удаление задачи 1 прошло успешно: " + taskManager.deleteTask(task1Created.getId()));
        System.out.println("Удаление эпика 2 прошло успешно: " + taskManager.deleteEpic(epic2Created.getId()));
        System.out.println("Удаление подзадачи 1-2 прошло успешно: " +
                taskManager.deleteSubtask(subtask12Created.getId()));
        System.out.println();

        System.out.println("Выведем оставшиеся задачи, эпики и подзадачи: ");
        System.out.println();
        System.out.println("Задачи: ");
        System.out.println(taskManager.getTasks());
        System.out.println("-".repeat(30));
        System.out.println("Эпики: ");
        System.out.println(taskManager.getEpics());
        System.out.println("-".repeat(30));
        System.out.println("Подзадачи: ");
        System.out.println(taskManager.getSubtasks());
        System.out.println("-".repeat(30));
        System.out.println();
        System.out.println();

        System.out.println("Тест 4");
        System.out.println("Проверяем работу допольнительного метода по выводу подзадач" +
                " для конкретного эпика.");
        System.out.println("Выводим подзадачи для эпика 1");
        taskManager.printSubtasksByEpic(epic1Created.getId());
        System.out.println("~".repeat(30));
    }
}

