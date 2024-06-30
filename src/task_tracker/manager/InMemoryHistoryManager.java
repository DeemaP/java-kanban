package task_tracker.manager;

import task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodesMap; // мапа для хранения узлов с объектами, которые запрашивали в истории
    private Node head;
    private Node tail;

    // Конструктор, который при создании объекта класса инициализирует пустую HashMap, а так же пустую голову и хвост
    public InMemoryHistoryManager() {
        head = null;
        tail = null;
        nodesMap = new HashMap<>();
    }

    // Добавляем просмотренные задачи в список
    @Override
    public void add(Task task) {
        if (task == null) return;
        if (nodesMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node newNode = linkLast(task);
        nodesMap.put(task.getId(), newNode); // добавление в мапу
    }

    // Возвращаем историю просмотров в виде списка
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.data);
            current = current.next;
        }
        return history;
    }

    // Метод для удаления задачи из истории, чтобы сохранялся только последний запрошенный экземпляр
    @Override
    public void remove(int id) {
        if (nodesMap.isEmpty()) return;
        if (nodesMap.get(id) == null) return;
        removeNode(nodesMap.get(id)); // Удаляем узел из двусвязного списка
        nodesMap.remove(id); // Удаляем узел из мапы
    }

    // Метод, который добавляет новый запрос задачи в конец списка
    private Node linkLast(Task task) {
        if (task == null) return null;
        Node newNode = new Node(task);
        if (head == null) { // если голова null, то наш список пуст
            head = newNode;  // обновляем голову
        } else {
            tail.next = newNode; // привязываем новый узел к хвосту
            newNode.prev = tail; // привязка хвоста списка к новому узлу
        }
        tail = newNode;// обновление хвоста списка
        return newNode;
    }

    // Метод, который удаляет узел
    private void removeNode(Node node) {
        if (nodesMap.isEmpty()) return;
        if (node == null) return;
        if (node.prev == null && node.next == null) { // удаляемый узел единственный в списке
            head = null;
            tail = null;
        } else if (node.prev == null) { // удаляемый узел - голова списка
            head = head.next; // переназначаем голову и убираем ссылку на прошлую
            head.prev = null;
        } else if (node.next == null) { // удаляемый узел - хвост списка
            tail = tail.prev; // переназначаем хвост и убираем ссылку на прошлый
            tail.next = null;
        } else {  // удаляемый узел в середине списка
            node.prev.next = node.next; // перенаправляем следующий и предыдущий узлы друг на друга
            node.next.prev = node.prev;
        }
        // убираем ссылки удаляемого узла
        node.next = null;
        node.prev = null;
    }

    // геттер для тестов
    public Map<Integer, Node> getNodesMap() {
        return nodesMap;
    }

    // Внутренний класс для реализации работы двусвязного списка
    public static class Node {
        Task data;
        Node next;
        Node prev;

        public Node(Task data) {
            this.data = data;
        }
    }
}
