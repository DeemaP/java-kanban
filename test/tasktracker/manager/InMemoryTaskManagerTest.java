package tasktracker.manager;

import org.junit.jupiter.api.Test;
import tasktracker.util.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return taskManager = (InMemoryTaskManager) Managers.getDefaultInMemoryTaskManager();
    }

    // Проверяем, что getDefault возвращает проинициализированный экземпляр класса InMemoryTaskManager
    @Test
    public void testGetDefault() {
        TaskManager taskManager = Managers.getDefaultInMemoryTaskManager();

        assertNotNull(taskManager, "Метод getDefault() вернул null вместо" +
                " проинициализированного экземпляра TaskManager.");

        assertTrue(taskManager instanceof InMemoryTaskManager, "Метод getDefault() вернул" +
                " экземпляр неправильного типа.");
    }

    // Проверяем, что getDefaultHistory возвращает проинициализированный экземпляр класса InMemoryHistoryManager
    @Test
    public void testGetDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultInMemoryHistoryManager();

        assertNotNull(historyManager, "Метод getDefaultHistory() вернул null вместо" +
                " проинициализированного экземпляра HistoryManager.");

        assertTrue(historyManager instanceof InMemoryHistoryManager, "Метод getDefaultHistory() вернул" +
                " экземпляр неправильного типа.");
    }
}
