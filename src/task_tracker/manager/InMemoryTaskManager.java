package task_tracker.manager;

import task_tracker.tasks.Epic;
import task_tracker.enums.Status;
import task_tracker.tasks.Subtask;
import task_tracker.tasks.Task;
import task_tracker.util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    private int newId;

    // Конструктор для создания taskManager со своим экземпляром historyManager
    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Генерация айди по возрастанию
    private Integer getNewId() {
        return newId++;
    }

    // Методы получения тасок, эпиков и сабтасок списком
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Методы создания тасок, эпиков и сабтасок
    @Override
    public Task createTask(Task task) {
        // Если айди не задан вручную генерируем новый айди.
        if (task.getId() == null) task.setId(getNewId());
        // Если tasks не содержит задачи с таким айди добавляем ее в мапу
        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            return task;
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        // Если айди не задан вручную генерируем новый айди.
        if (epic.getId() == null) epic.setId(getNewId());
        // Если epics не содержит эпика с таким айди - добавляем ее в мапу
        if (!epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            return epic;
        }
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {  // Если эпик для этой подзадачи существует - создаем ее.
            // Если айди не задан вручную генерируем новый айди.
            if (subtask.getId() == null) subtask.setId(getNewId());
            // Если subtasks не содержит сабтаски с таким id и id эпика не равен id сабтаски - добавляем ее в мапу
            if (!subtasks.containsKey(subtask.getId()) && (!epic.getId().equals(subtask.getId()))) {
                subtasks.put(subtask.getId(), subtask);
                epic.addSubtask(subtask.getId()); // добавляем сабтаску к эпику
                updateEpicStatus(epic);  // обновляем статус эпика в соответствии со статусом сабтаски
                return subtask;
            }
        }
        return subtask;
    }

    // Методы обновления тасок, эпиков и сабтасок
    @Override
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return null;
        }
        tasks.replace(task.getId(), task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return null;
        }
        epics.get(epic.getId()).setName(epic.getName());
        epics.get(epic.getId()).setDescription(epic.getDescription());
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return null;
        }
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    // Методы для удаления тасок, эпиков и сабтасок
    @Override
    public boolean deleteTask(int taskId) {
        return tasks.remove(taskId) != null;
    }

    @Override
    public boolean deleteEpic(int epicId) {
        Epic deletedEpic = epics.remove(epicId);
        if (deletedEpic != null) {
            // удаляем все сабтаски у этого эпика
            for (Integer subtaskId : deletedEpic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteSubtask(Integer subtaskId) {
        Subtask deletedSubtask = subtasks.remove(subtaskId);
        if (deletedSubtask != null) {
            Epic epic = epics.get(deletedSubtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(subtaskId);
                updateEpicStatus(epic);
            }
            return true;
        }
        return false;
    }

    // Методы для удаления всех задач по типам
    @Override
    public boolean deleteAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteAllEpics() {
        if (!epics.isEmpty()) {
            if (!subtasks.isEmpty()) {  // сабтаски не существуют без эпиков, поэтому удаляем сабтаски у эпика
                subtasks.clear();
            }
            epics.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            for (Epic epic : epics.values()) {
                if (!epic.getSubtaskIds().isEmpty()) {
                    epic.getSubtaskIds().clear();
                }
            }
            subtasks.clear();
            return true;
        }
        return false;
    }

    // Метод для обновления статуса эпика при добавлении сабтаски
    private void updateEpicStatus(Epic epic) {
        List<Integer> epicSubtaskIds = epic.getSubtaskIds();
        if (epicSubtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer id : epicSubtaskIds) {
            if (subtasks.get(id).getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtasks.get(id).getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    // Дополнительный метод для вывода подзадач по эпику
    public List<Subtask> getSubtasksByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> subtasksByEpic = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicId() == epicId) {
                    subtasksByEpic.add(subtask);
                }
            }
            return subtasksByEpic;
        }
        return null; // возвращаем null если эпика не существует
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtask;
        }
        return null;
    }
}
