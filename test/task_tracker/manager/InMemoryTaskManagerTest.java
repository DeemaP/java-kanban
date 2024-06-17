package task_tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task_tracker.tasks.Epic;
import task_tracker.enums.Status;
import task_tracker.tasks.Subtask;
import task_tracker.tasks.Task;
import task_tracker.util.Managers;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    // Получаем экземпляры taskManager и historyManager для тестов
    @BeforeEach
    void setup() {
        taskManager = Managers.getDefault();
    }

    // Создаем две задачи с одинаковым айди, но разным содержимым и проверяем их равенство по айди.
    @Test
    public void testTasksEqualsById() {
        Task task1 = new Task("Task 1", "Описание задачи 1", Status.NEW);
        task1.setId(1);
        Task task2 = new Task("Task 2", "Описание задачи 2", Status.IN_PROGRESS);
        task2.setId(1);

        assertEquals(task1, task2);
    }

    // Создаем два эпика с одинаковым айди, но разным содержимым и проверяем их равенство по айди.
    @Test
    public void testEpicEqualsById() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }

    // Создаем две подзадачи с одинаковым айди, но разным содержимым и проверяем их равенство по айди.
    @Test
    public void testSubtaskEqualsById() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.DONE, 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, 1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2);
    }

    /* Проверим, что в эпик нельзя добавить подзадачу с таким же id, как у эпика.
    Эпик нельзя добавить в самого себя в качестве подзадачи и нельзя подзадачу сделать своим эпиком. */
    @Test
    public void testEpicCannotBeAddedAsSubtaskToItself() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), "Подзадача с id эпика",
                "Описание подзадачи с id эпика", Status.NEW, epic.getId());
        Subtask createdSubtask = taskManager.createSubtask(subtask);
        assertNull((taskManager.getSubtask(createdSubtask.getId())),
                "Подзадача с id эпика не должна быть добавлена.");
    }

    // Проверяем, что getDefault возвращает проинициализированный экземпляр класса InMemoryTaskManager
    @Test
    public void testGetDefault() {
        TaskManager taskManager = Managers.getDefault();

        assertNotNull(taskManager, "Метод getDefault() вернул null вместо" +
                " проинициализированного экземпляра TaskManager.");

        assertTrue(taskManager instanceof InMemoryTaskManager, "Метод getDefault() вернул" +
                " экземпляр неправильного типа.");
    }

    // Проверяем, что getDefaultHistory возвращает проинициализированный экземпляр класса InMemoryHistoryManager
    @Test
    public void testGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Метод getDefaultHistory() вернул null вместо" +
                " проинициализированного экземпляра HistoryManager.");

        assertTrue(historyManager instanceof InMemoryHistoryManager, "Метод getDefaultHistory() вернул" +
                " экземпляр неправильного типа.");
    }

    // Проверяем, что InMemoryTaskManager добавляет задачи и может найти их по айди
    @Test
    public void testCreateAndFindTaskById() {
        Task task = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        taskManager.createTask(task);

        Task createdTask = taskManager.getTask(task.getId());

        assertNotNull(createdTask, "Задача не найдена по id.");
        assertEquals(task, createdTask, "Найденная задача не совпадает с добавленной.");
    }

    // Проверяем, что InMemoryTaskManager добавляет эпики и может найти их по айди
    @Test
    public void testCreateAndFindEpicById() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        Task createdEpic = taskManager.getEpic(epic.getId());

        assertNotNull(createdEpic, "Эпик не найден по id.");
        assertEquals(epic, createdEpic, "Найденный эпик не совпадает с добавленным.");
    }

    // Проверяем, что InMemoryTaskManager добавляет подзадачи и может найти их по айди
    @Test
    public void testCreateAndFindSubtaskById() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        Task createdSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(createdSubtask, "Подзадача не найдена по id.");
        assertEquals(subtask, createdSubtask, "Найденная подзадача не совпадает с добавленной.");
    }

    // Проверим, что в менеджере задачи с заданным и сгенерированным id не конфликтуют
    @Test
    public void testSetAndGeneratedIdsForTasksDoNotConflict() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        task1.setId(5);
        taskManager.createTask(task1); // Добавили задачу с заданным id
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        taskManager.createTask(task2); // Добавили задачу и id ей сгенерирует метод добавления

        // Проверяем, что обе задачи добавлены и могут быть найдены по айди
        assertNotNull(taskManager.getTask(task1.getId()), "Задача с заданным айди должна быть найдена");
        assertNotNull(taskManager.getTask(task2.getId()), "Задача с сгенерированным айди должна быть найдена");

        // Проверим, что у задач разные айди
        assertNotEquals(task1.getId(), task2.getId(), "У задач должен быть разный id");
    }

    // Проверим, что в менеджере эпики и подзадачи с заданным и сгенерированным id не конфликтуют
    @Test
    public void testSetAndGeneratedIdsForEpicsAndSubtasksDoNotConflict() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic1.setId(5);
        taskManager.createEpic(epic1); // Добавили эпик с заданным id
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic2); // Добавили эпик и id ему сгенерирует метод добавления

        Subtask subtask1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1", Status.NEW,
                epic1.getId());
        subtask1.setId(14);
        taskManager.createSubtask(subtask1); // Добавили подзадачу с заданным id
        Subtask subtask2 = new Subtask("Подзадача 2-1", "Описание подзадачи 2-1", Status.IN_PROGRESS,
                epic2.getId());
        taskManager.createSubtask(subtask2); // Добавили подзадачу и id ей сгенерирует метод добавления.

        // Проверяем, что оба эпика добавлены и могут быть найдены по айди
        assertNotNull(taskManager.getEpic(epic1.getId()), "Эпик с заданным айди должен быть найден");
        assertNotNull(taskManager.getEpic(epic2.getId()), "Эпик с сгенерированным айди должен быть найден");

        // Проверим, что у эпиков разные айди
        assertNotEquals(epic1.getId(), epic2.getId(), "У эпиков должен быть разный id");

        // Проверяем, что обе подзадачи добавлены и могут быть найдены по айди
        assertNotNull(taskManager.getSubtask(subtask1.getId()),
                "Подзадача с заданным айди должна быть найдена");
        assertNotNull(taskManager.getSubtask(subtask2.getId()),
                "Подзадача с сгенерированным айди должна быть найдена");

        // Проверим, что у подзадач разные айди
        assertNotEquals(subtask1.getId(), subtask2.getId(), "У подзадач должен быть разный id");
    }

    // Проверим что задача неизменна после добавления в менеджер
    @Test
    public void testTaskUnchangedAfterCreate() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.DONE);
        taskManager.createTask(task1);
        Task taskFromManager = taskManager.getTask(task1.getId()); // Получили задачу из менеджера для сравнения полей

        assertEquals(task1.getId(), taskFromManager.getId(), "id должны совпадать");
        assertEquals(task1.getName(), taskFromManager.getName(), "имена должны совпадать");
        assertEquals(task1.getDescription(), taskFromManager.getDescription(), "описания должны совпадать");
        assertEquals(task1.getStatus(), taskFromManager.getStatus(), "статусы должны совпадать");
    }

    // Проверим что эпики и подзадачи неизменны после добавления в менеджер
    @Test
    public void testEpicsAndSubtasksUnchangedAfterCreate() {
        // Создаем эпик и подзадачу, добавляем в менеджер
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1", Status.IN_PROGRESS,
                epic1.getId());
        taskManager.createSubtask(subtask1);

        // Получаем эпик и подзадачу из менеджера для сравнения полей
        Epic epicFromManager = taskManager.getEpic(epic1.getId());
        Subtask subtaskFromManager = taskManager.getSubtask(subtask1.getId());

        assertEquals(epic1.getId(), epicFromManager.getId(), "id эпиков должны совпадать");
        assertEquals(epic1.getName(), epicFromManager.getName(), "имена эпиков должны совпадать");
        assertEquals(epic1.getDescription(), epicFromManager.getDescription(),
                "описания эпиков должны совпадать");
        assertEquals(epic1.getStatus(), epicFromManager.getStatus(), "статусы эпиков должны совпадать");
        assertEquals(epic1.getSubtaskIds(), epicFromManager.getSubtaskIds(),
                "списки id подзадач эпиков должны совпадать");

        assertEquals(subtask1.getId(), subtaskFromManager.getId(), "id подзадач должны совпадать");
        assertEquals(subtask1.getName(), subtaskFromManager.getName(), "имена подзадач должны совпадать");
        assertEquals(subtask1.getDescription(), subtaskFromManager.getDescription(),
                "описания подзадач должны совпадать");
        assertEquals(subtask1.getStatus(), subtaskFromManager.getStatus(), "статусы подзадач должны совпадать");
        assertEquals(subtask1.getEpicId(), subtaskFromManager.getEpicId(), "id эпиков подзадач должны совпадать");
    }

    // Проверяем, что HistoryManager сохраняет предыдущую версию задачи и ее данных
    @Test
    public void testHistoryManagerSavesPreviousVersionOfTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        taskManager.createTask(task1); // Создали задачу
        taskManager.getTask(task1.getId()); // Добавили в историю старую версию

        // Изменим данные задачи
        task1.setName("ЗАДАЧА 1");
        task1.setDescription("ОПИСАНИЕ ЗАДАЧИ 1");
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        taskManager.getTask(task1.getId()); // Добавили в историю текущую версию

        // Получаем историю задач
        List<Task> history = new LinkedList<>(((InMemoryTaskManager) taskManager).getHistory());

        // Получаем задачи из истории
        Task previousTask = history.get(0); // первое сохраненное значение в истории
        Task currentTask = history.get(1); //  второе сохраненное значение в истории

        assertEquals(previousTask.getId(), currentTask.getId(), "id задач должны совпадать");
        assertNotEquals(previousTask.getName(), currentTask.getName(), "имена задач должны отличаться");
        assertNotEquals(previousTask.getDescription(), currentTask.getDescription(),
                "описания задач должны отличаться");
        assertNotEquals(previousTask.getStatus(), currentTask.getStatus(), "статусы задач должны отличаться");
    }
}