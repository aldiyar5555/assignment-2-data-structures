import java.util.Arrays;

public class DynamicArray {
    private int[] data;
    private int size;
    private long accessCount;

    public DynamicArray() {
        this(10);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        data = new int[Math.max(1, initialCapacity)];
    }

    public void add(int value) {
        ensureCapacity(size + 1);
        data[size++] = value;
        accessCount++;
    }

    public void add(int index, int value) {
        checkPositionIndex(index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            accessCount += 2;
        }
        data[index] = value;
        accessCount++;
        size++;
    }

    public int remove(int index) {
        checkElementIndex(index);
        int removed = data[index];
        accessCount++;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            accessCount += 2;
        }
        data[--size] = 0;
        accessCount++;
        return removed;
    }

    public int get(int index) {
        checkElementIndex(index);
        accessCount++;
        return data[index];
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            accessCount++;
            if (data[i] == value) {
                return true;
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

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= data.length) {
            return;
        }
        int newCapacity = Math.max(minimumCapacity, data.length * 2);
        int[] expanded = Arrays.copyOf(data, newCapacity);
        accessCount += size * 2L;
        data = expanded;
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