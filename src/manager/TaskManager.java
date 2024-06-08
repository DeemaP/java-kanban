package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int newId;

    // Генерация айди по возрастанию
    private Integer getNewId() {
        return newId++;
    }

    // Методы получения тасок, эпиков и сабтасок списком
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Методы создания тасок, эпиков и сабтасок
    public Task createTask(Task task) {
        task.setId(getNewId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNewId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getNewId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {  // Проверяем существует ли эпик для этой подзадачи
            epic.addSubtask(subtask.getId()); // добавляем сабтаску к эпику
            updateEpicStatus(epic); // обновляем статус эпика в соответствии со статусом сабтаски
        }
        return subtask;
    }

    // Методы обновления тасок, эпиков и сабтасок
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return null;
        }
        tasks.replace(task.getId(), task);
        return task;
    }

    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return null;
        }
        epics.get(epic.getId()).setName(epic.getName());
        epics.get(epic.getId()).setDescription(epic.getDescription());
        return epic;
    }

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
    public boolean deleteTask(int taskId) {
        return tasks.remove(taskId) != null;
    }

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
    public boolean deleteAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
            return true;
        }
        return false;
    }


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

    public boolean deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
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

}
