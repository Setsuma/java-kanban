package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    final private HashMap<Integer, Node> nodes = new HashMap<>();
    final private CustomLinkedList tasksList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            int taskId = task.getId();
            if (nodes.containsKey(taskId)) {
                tasksList.removeNode(nodes.get(taskId));
            }
            nodes.put(taskId, tasksList.linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        if (nodes.containsKey(id)) {
            tasksList.removeNode(nodes.get(id));
            nodes.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksList.getTasks();
    }

    private class CustomLinkedList {
        public Node head;
        public Node tail;

        private Node linkLast(Task data) {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
            } else {
                newNode.prev = tail;
                tail.next = newNode;
            }
            tail = newNode;
            return newNode;
        }

        private void removeNode(Node node) {
            if (node.next == null && node.prev == null) {
                tail = null;
                head = null;
            } else if (node.next == null) {
                tail.prev.next = null;
                tail = tail.prev;
            } else if (node.prev == null) {
                head.next.prev = null;
                head = head.next;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
        }

        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node nextNode = tail;
            while (nextNode != null) {
                tasks.add(nextNode.data);
                nextNode = nextNode.prev;
            }
            return tasks;
        }
    }


    private class Node {
        Task data;
        Node next;
        Node prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}



