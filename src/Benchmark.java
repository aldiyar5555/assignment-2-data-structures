import java.util.Locale;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int RUNS = 5;
    private static final int MAX_SAMPLED_OPERATIONS = 100;
    private static volatile long resultSink;

    private Benchmark() {
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ROOT);
        System.out.println("Each row is the average of 5 runs; sampled workloads use at most 100 operations per run.");
        runWorkload1();
        runWorkload2();
        runWorkload3();
        runWorkload4();
        if (resultSink == Long.MIN_VALUE) {
            System.out.println("Result sink: " + resultSink);
        }
    }

    private static void runWorkload1() {
        printHeader("Workload 1: append/insert N values");
        for (int n : SIZES) {
            double[] times = new double[3];
            double[] counts = new double[3];
            for (int run = 0; run < RUNS; run++) {
                Random random = new Random(42L + run);
                int[] values = randomValues(n, random);

                DynamicArray array = new DynamicArray();
                long start = System.nanoTime();
                for (int value : values) {
                    array.add(value);
                }
                times[0] += elapsedMillis(start);
                counts[0] += array.getAccessCount();

                LinkedList list = new LinkedList();
                start = System.nanoTime();
                for (int value : values) {
                    list.add(value);
                }
                times[1] += elapsedMillis(start);
                counts[1] += list.getAccessCount();

                MinHeap heap = new MinHeap();
                start = System.nanoTime();
                for (int value : values) {
                    heap.insert(value);
                }
                times[2] += elapsedMillis(start);
                counts[2] += heap.getComparisonCount();
            }
            printRow(n, times, counts);
        }
    }

    private static void runWorkload2() {
        printHeader("Workload 2: random indexed reads (DynamicArray vs LinkedList)");
        for (int n : SIZES) {
            double[] times = new double[3];
            double[] counts = new double[3];
            for (int run = 0; run < RUNS; run++) {
                Random random = new Random(42L + run);
                int[] values = randomValues(n, random);
                int[] indices = randomIndices(n, Math.min(n, MAX_SAMPLED_OPERATIONS), random);
                DynamicArray array = new DynamicArray(n);
                LinkedList list = new LinkedList();
                for (int value : values) {
                    array.add(value);
                    list.add(value);
                }
                array.resetAccessCount();
                list.resetAccessCount();

                long checksum = 0;
                long start = System.nanoTime();
                for (int index : indices) {
                    checksum += array.get(index);
                }
                times[0] += elapsedMillis(start);
                counts[0] += array.getAccessCount();

                start = System.nanoTime();
                for (int index : indices) {
                    checksum += list.get(index);
                }
                times[1] += elapsedMillis(start);
                counts[1] += list.getAccessCount();
                resultSink += checksum;
            }
            printRow(n, times, counts);
        }
    }

    private static void runWorkload3() {
        printHeader("Workload 3: contains queries (DynamicArray vs LinkedList)");
        for (int n : SIZES) {
            double[] times = new double[3];
            double[] counts = new double[3];
            for (int run = 0; run < RUNS; run++) {
                Random random = new Random(42L + run);
                int[] values = randomValues(n, random);
                int queryCount = Math.min(n, MAX_SAMPLED_OPERATIONS);
                int[] queries = new int[queryCount];
                for (int i = 0; i < queryCount; i++) {
                    queries[i] = values[random.nextInt(n)];
                }
                DynamicArray array = new DynamicArray(n);
                LinkedList list = new LinkedList();
                for (int value : values) {
                    array.add(value);
                    list.add(value);
                }
                array.resetAccessCount();
                list.resetAccessCount();

                long found = 0;
                long start = System.nanoTime();
                for (int query : queries) {
                    if (array.contains(query)) {
                        found++;
                    }
                }
                times[0] += elapsedMillis(start);
                counts[0] += array.getAccessCount();

                start = System.nanoTime();
                for (int query : queries) {
                    if (list.contains(query)) {
                        found++;
                    }
                }
                times[1] += elapsedMillis(start);
                counts[1] += list.getAccessCount();
                resultSink += found;
            }
            printRow(n, times, counts);
        }
    }

    private static void runWorkload4() {
        printHeader("Workload 4: indexed removals (DynamicArray vs LinkedList); heap extractMin");
        for (int n : SIZES) {
            double[] times = new double[3];
            double[] counts = new double[3];
            for (int run = 0; run < RUNS; run++) {
                Random random = new Random(42L + run);
                int[] values = randomValues(n, random);
                int operationCount = Math.min(n, MAX_SAMPLED_OPERATIONS);
                DynamicArray array = new DynamicArray(n);
                LinkedList list = new LinkedList();
                MinHeap heap = new MinHeap(n);
                for (int value : values) {
                    array.add(value);
                    list.add(value);
                    heap.insert(value);
                }
                array.resetAccessCount();
                list.resetAccessCount();
                heap.resetComparisonCount();

                long checksum = 0;
                long start = System.nanoTime();
                for (int i = 0; i < operationCount; i++) {
                    checksum += array.remove(random.nextInt(array.size()));
                }
                times[0] += elapsedMillis(start);
                counts[0] += array.getAccessCount();

                random = new Random(42L + run);
                start = System.nanoTime();
                for (int i = 0; i < operationCount; i++) {
                    checksum += list.remove(random.nextInt(list.size()));
                }
                times[1] += elapsedMillis(start);
                counts[1] += list.getAccessCount();

                start = System.nanoTime();
                for (int i = 0; i < operationCount; i++) {
                    checksum += heap.extractMin();
                }
                times[2] += elapsedMillis(start);
                counts[2] += heap.getComparisonCount();
                resultSink += checksum;
            }
            printRow(n, times, counts);
        }
    }

    private static int[] randomValues(int size, Random random) {
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt();
        }
        return values;
    }

    private static int[] randomIndices(int size, int count, Random random) {
        int[] indices = new int[count];
        for (int i = 0; i < count; i++) {
            indices[i] = random.nextInt(size);
        }
        return indices;
    }

    private static double elapsedMillis(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000.0;
    }

    private static void printHeader(String title) {
        System.out.println("\n" + title);
        System.out.printf("%-8s | %14s %14s | %14s %14s | %14s %14s%n",
                "N", "Array ms", "Array ops", "List ms", "List ops", "Heap ms", "Heap cmp");
    }

    private static void printRow(int n, double[] totalMillis, double[] totalCounts) {
        System.out.printf(Locale.ROOT, "%-8d | %14.3f %14.0f | %14.3f %14.0f | %14.3f %14.0f%n",
                n,
                totalMillis[0] / RUNS, totalCounts[0] / RUNS,
                totalMillis[1] / RUNS, totalCounts[1] / RUNS,
                totalMillis[2] / RUNS, totalCounts[2] / RUNS);
    }
}