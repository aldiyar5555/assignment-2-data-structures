import java.util.NoSuchElementException;

public class Tests {
    private Tests() {
    }

    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();
        System.out.println("ALL TESTS PASSED");
    }

    private static void testDynamicArray() {
        DynamicArray array = new DynamicArray(0);
        check(array.isEmpty(), "New array should be empty");
        check(!array.contains(1), "Empty array should not contain a value");
        expectThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        expectThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        expectThrows(IndexOutOfBoundsException.class, () -> array.add(1, 5));

        array.add(4);
        check(array.get(0) == 4, "Single-element array get");
        check(array.remove(0) == 4, "Single-element array remove");
        check(array.isEmpty(), "Array should be empty after removing its only element");

        array.add(1);
        array.add(1);
        array.add(0, 2);
        check(array.size() == 3, "Array size after indexed additions");
        check(array.get(0) == 2 && array.get(1) == 1 && array.get(2) == 1,
                "Array indexed insertion order and duplicates");
        check(array.contains(1), "Array should find a duplicate value");
        check(!array.contains(99), "Array should not find a missing value");
        check(array.remove(1) == 1 && array.get(1) == 1, "Array removal with duplicates");
        expectThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
        expectThrows(IndexOutOfBoundsException.class, () -> array.get(array.size()));
        expectThrows(IndexOutOfBoundsException.class, () -> array.remove(array.size()));
        expectThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 7));
        expectThrows(IndexOutOfBoundsException.class, () -> array.add(array.size() + 1, 7));
        check(array.getAccessCount() > 0, "Array should count element accesses");
        array.resetAccessCount();
        check(array.getAccessCount() == 0, "Array access counter reset");
    }

    private static void testLinkedList() {
        LinkedList list = new LinkedList();
        check(list.isEmpty(), "New list should be empty");
        check(!list.contains(1), "Empty list should not contain a value");
        expectThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        expectThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        expectThrows(IndexOutOfBoundsException.class, () -> list.add(1, 5));

        list.add(7);
        check(list.get(0) == 7, "Single-element list get");
        check(list.remove(0) == 7, "Single-element list remove");
        check(list.isEmpty(), "List should be empty after removing its only element");

        list.add(3);
        list.add(3);
        list.add(0, 8);
        list.add(2, 5);
        check(list.size() == 4, "List size after indexed additions");
        check(list.get(0) == 8 && list.get(1) == 3 && list.get(2) == 5 && list.get(3) == 3,
                "List indexed insertion order and duplicates");
        check(list.contains(3), "List should find a duplicate value");
        check(!list.contains(99), "List should not find a missing value");
        check(list.remove(0) == 8 && list.remove(list.size() - 1) == 3,
                "List removal at both ends");
        check(list.remove(0) == 3 && list.get(0) == 5, "List removal with duplicates");
        expectThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        expectThrows(IndexOutOfBoundsException.class, () -> list.get(list.size()));
        expectThrows(IndexOutOfBoundsException.class, () -> list.remove(list.size()));
        expectThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 7));
        expectThrows(IndexOutOfBoundsException.class, () -> list.add(list.size() + 1, 7));
        check(list.getAccessCount() > 0, "List should count node accesses");
        list.resetAccessCount();
        check(list.getAccessCount() == 0, "List access counter reset");
    }

    private static void testMinHeap() {
        MinHeap heap = new MinHeap(0);
        check(heap.isEmpty(), "New heap should be empty");
        expectThrows(NoSuchElementException.class, () -> heap.peekMin());
        expectThrows(NoSuchElementException.class, () -> heap.extractMin());

        heap.insert(5);
        check(heap.peekMin() == 5, "Single-element heap minimum");
        check(heap.extractMin() == 5 && heap.isEmpty(), "Single-element heap extraction");

        heap.insert(4);
        heap.insert(1);
        heap.insert(1);
        heap.insert(-2);
        check(heap.peekMin() == -2, "Heap minimum after insertions");
        check(heap.extractMin() == -2, "Heap extracts smallest value first");
        check(heap.extractMin() == 1 && heap.extractMin() == 1, "Heap handles duplicates");
        check(heap.extractMin() == 4 && heap.isEmpty(), "Heap extraction order and empty state");
        check(heap.getComparisonCount() > 0, "Heap should count comparisons");
        heap.resetComparisonCount();
        check(heap.getComparisonCount() == 0, "Heap comparison counter reset");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectThrows(Class<? extends Throwable> expected, Runnable action) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expected.isInstance(actual)) {
                return;
            }
            throw new AssertionError("Expected " + expected.getSimpleName() + " but got "
                    + actual.getClass().getSimpleName(), actual);
        }
        throw new AssertionError("Expected " + expected.getSimpleName() + " to be thrown");
    }
}