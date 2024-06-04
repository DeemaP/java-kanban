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
        epic.addSubtask(subtask); // добавляем сабтаску к эпику
        updateEpicStatus(epic); // обновляем статус эпика в соответствии со статусом сабтаски
        return subtask;
    }

    // Методы обновления тасок, эпиков и сабтасок
    public Task updateTask(Task task) {
        if (task.getId() == null || !tasks.containsKey(task.getId())) {
            return null;
        }
        tasks.replace(task.getId(), task);
        return task;
    }

    public Epic updateEpic(Epic epic) {
        if (epic.getId() == null || !epics.containsKey(epic.getId())) {
            return null;
        }
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        return epic;
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (subtask.getId() == null || !subtasks.containsKey(subtask.getId())) {
            return null;
        }
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.updateSubtask(subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    // Методы для удаления тасок, эпиков и сабтасок
    public boolean deleteTask(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            tasks.remove(taskId);
            return true;
        }
        return false;
    }

    public boolean deleteEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            // сначала удаляем все сабтаски у этого эпика
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(epicId); // затем удаляем сам эпик
            return true;
        }
        return false;
    }

    public boolean deleteSubtask(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                subtasks.remove(subtaskId);
                updateEpicStatus(epic);
            }
            return true;
        }
        return false;
    }

    // Метод для обновления статуса эпика при добавлении сабтаски
    private void updateEpicStatus(Epic epic) {
        List<Subtask> epicSubtasks = epic.getSubtasks();
        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
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
    public void printSubtasksByEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.println("Эпик с айди " + '\'' + epicId + '\'' + " не найден");
            return;
        }
        List<Subtask> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            System.out.println("у эпика с айди " + '\'' + epicId + '\'' + " и именем " + '\'' +
                    epic.getName() + '\'' + " нет подзадач");
            return;
        }
        System.out.println("Подзадачи эпика с айди " + '\'' + epicId + '\'' + " и именем " +
                '\'' + epic.getName() + '\'' + ":");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
    }
}
