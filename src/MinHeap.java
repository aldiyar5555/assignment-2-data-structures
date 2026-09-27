import java.util.NoSuchElementException;

public class MinHeap {
    private int[] data;
    private int size;
    private long comparisonCount;

    public MinHeap() {
        this(10);
    }

    public MinHeap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        data = new int[Math.max(1, initialCapacity)];
    }

    public void insert(int value) {
        ensureCapacity(size + 1);
        int index = size++;
        data[index] = value;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(data[index], data[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    public int peekMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty");
        }
        int minimum = data[0];
        data[0] = data[--size];
        if (size > 0) {
            siftDown(0);
        }
        return minimum;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public void resetComparisonCount() {
        comparisonCount = 0;
    }

    private void siftDown(int index) {
        while (true) {
            int smallest = index;
            int left = index * 2 + 1;
            int right = left + 1;
            if (left < size && compare(data[left], data[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && compare(data[right], data[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                return;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private int compare(int first, int second) {
        comparisonCount++;
        return Integer.compare(first, second);
    }

    private void swap(int first, int second) {
        int temporary = data[first];
        data[first] = data[second];
        data[second] = temporary;
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity > data.length) {
            int newCapacity = Math.max(minimumCapacity, data.length * 2);
            data = java.util.Arrays.copyOf(data, newCapacity);
        }
    }
}