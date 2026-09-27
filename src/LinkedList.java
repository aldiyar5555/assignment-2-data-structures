public class LinkedList {
    private static final class Node {
        private int value;
        private Node next;
        private Node previous;

        private Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private long accessCount;

    public void add(int value) {
        Node node = new Node(value);
        if (tail == null) {
            head = tail = node;
            accessCount++;
        } else {
            tail.next = node;
            node.previous = tail;
            tail = node;
            accessCount += 3;
        }
        size++;
    }

    public void add(int index, int value) {
        checkPositionIndex(index);
        if (index == size) {
            add(value);
            return;
        }
        if (index == 0) {
            Node node = new Node(value);
            node.next = head;
            head.previous = node;
            head = node;
            accessCount += 2;
            size++;
            return;
        }

        Node nextNode = nodeAt(index);
        Node previousNode = nextNode.previous;
        Node node = new Node(value);
        node.previous = previousNode;
        node.next = nextNode;
        previousNode.next = node;
        nextNode.previous = node;
        accessCount += 4;
        size++;
    }

    public int remove(int index) {
        checkElementIndex(index);
        Node node = nodeAt(index);
        int removed = node.value;
        if (node.previous == null) {
            head = node.next;
        } else {
            node.previous.next = node.next;
        }
        if (node.next == null) {
            tail = node.previous;
        } else {
            node.next.previous = node.previous;
        }
        accessCount += 4;
        size--;
        if (size == 0) {
            head = tail = null;
            accessCount++;
        }
        return removed;
    }

    public int get(int index) {
        checkElementIndex(index);
        return nodeAt(index).value;
    }

    public boolean contains(int value) {
        Node current = head;
        while (current != null) {
            accessCount++;
            if (current.value == value) {
                return true;
            }
            current = current.next;
            if (current != null) {
                accessCount++;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void resetAccessCount() {
        accessCount = 0;
    }

    private Node nodeAt(int index) {
        if (index < size / 2) {
            Node current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
                accessCount++;
            }
            accessCount++;
            return current;
        }
        Node current = tail;
        for (int i = size - 1; i > index; i--) {
            current = current.previous;
            accessCount++;
        }
        accessCount++;
        return current;
    }

    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}