# Assignment 2: Data Structures and Benchmark

**Student:** Aldiyar Aimbetov  
**GitHub profile:** [aldiyar5555](https://github.com/aldiyar5555)  
**Repository:** [assignment-2](https://github.com/aldiyar5555/assignment-2)

## Theoretical Complexity Table

Let `N` be the number of elements. Space below is auxiliary space per operation and excludes the `O(N)` storage occupied by each data structure. Dynamic-array and heap resizing can temporarily require `O(N)` extra space when capacity grows. The heap does not support arbitrary indexed `get`.

| Data structure | Operation | Best time | Average time | Worst time | Auxiliary space |
|---|---|---:|---:|---:|---:|
| Dynamic Array | `get(index)` | `O(1)` | `O(1)` | `O(1)` | `O(1)` |
| Dynamic Array | `contains(x)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Dynamic Array | `add(x)` (append) | `O(1)` | `O(1)` amortized | `O(N)` | `O(1)` amortized; `O(N)` on resize |
| Dynamic Array | `add(index, x)` | `O(1)` | `O(N)` | `O(N)` | `O(1)`; `O(N)` on resize |
| Dynamic Array | `remove(index)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Linked List | `get(index)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Linked List | `contains(x)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Linked List | `add(x)` (append) | `O(1)` | `O(1)` | `O(1)` | `O(1)` |
| Linked List | `add(index, x)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Linked List | `remove(index)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Min-Heap | `get(index)` | Not supported | Not supported | Not supported | Not supported |
| Min-Heap | `contains(x)` | `O(1)` | `O(N)` | `O(N)` | `O(1)` |
| Min-Heap | `insert(x)` | `O(1)` | `O(log N)` | `O(log N)`; `O(N)` on resize | `O(1)`; `O(N)` on resize |
| Min-Heap | `extractMin()` | `O(1)` | `O(log N)` | `O(log N)` | `O(1)` |

For indexed array insertion and removal, the best case is at the end of the array, where no elements need shifting. For the linked list, the best case is at an endpoint; finding an interior position takes linear time. Heap bounds assume the usual binary-heap operations; occasional backing-array growth can take linear time.

## Correctness Proofs & Loop Invariants

### `DynamicArray.contains(x)`

The method scans indices from `0` through `size - 1` and returns `true` as soon as it finds `x`.

**Loop invariant:** At the start of the iteration with index `i`, if the method has not returned, then none of the elements in the prefix `data[0..i-1]` equals `x`.

- **Initialization:** Before the first iteration, `i = 0`. The prefix is empty, so it contains no occurrence of `x`; the invariant holds.
- **Maintenance:** During iteration `i`, the method compares `data[i]` with `x`. If they are equal, it returns `true`, which is correct. Otherwise, the prefix `data[0..i]` contains no `x`; after incrementing `i`, that prefix is precisely the prefix covered by the invariant for the next iteration.
- **Termination:** If the loop ends, `i == size`. The invariant says that no element in `data[0..size-1]` equals `x`, so returning `false` is correct. If the array is empty, the loop does not execute and the same conclusion holds.

### `MinHeap.siftDown(0)`

`siftDown` repeatedly compares a node with its children and swaps it with the smaller child when the min-heap property is violated. The method is used after replacing the root during `extractMin`.

**Loop invariant:** At the start of each iteration, both child subtrees of the current node are min-heaps, and any possible heap-order violation caused by the replacement element is confined to the current node and the path below it. Every completed level above the current node satisfies the min-heap property.

- **Initialization:** The replacement element is placed at the root. Its left and right subtrees were min-heaps before extraction and remain unchanged, so they satisfy the invariant. There are no levels above the root.
- **Maintenance:** The algorithm chooses the smallest among the current node and its existing children. If the current node is already smallest, it is no greater than either child, so the current subtree is a min-heap and the algorithm terminates. Otherwise, it swaps with the smaller child. This places a value no greater than either child at the current position, restoring heap order there. The displaced value moves down to the child's position; the child's untouched child subtrees are heaps, so any remaining violation is confined to the new current node and its descendants. The completed levels above remain valid.
- **Termination:** Each swap moves the current position strictly down one level in a finite heap, so the loop must terminate. It either stops when the current node is no greater than its children, or reaches a leaf, which has no children to violate heap order. Combined with the already valid levels above, the whole array again represents a min-heap.

## Empirical Benchmark Results

The following values are the exact benchmark results supplied for the latest terminal run. Times are in milliseconds; `ops` is the implementation's element-access count and `cmp` is the heap comparison count.

### Workload 1: Appending N Elements

| N | DynamicArray (ms, ops) | LinkedList (ms, ops) | MinHeap (ms, cmp) |
|---:|---:|---:|---:|
| 100 | 0.012 ms, 400 ops | 0.095 ms, 298 ops | 0.035 ms, 202 cmp |
| 1,000 | 0.076 ms, 3,540 ops | 0.132 ms, 2,998 ops | 0.132 ms, 2,239 cmp |
| 10,000 | 0.489 ms, 30,460 ops | 0.421 ms, 29,998 ops | 0.724 ms, 22,683 cmp |
| 100,000 | 1.838 ms, 427,660 ops | 4.158 ms, 299,998 ops | 3.344 ms, 228,142 cmp |

### Workload 2: Random Access `get(i)`

| N | DynamicArray (ms, ops) | LinkedList (ms, ops) |
|---:|---:|---:|
| 100 | 0.016 ms, 100 ops | 0.071 ms, 2,541 ops |
| 1,000 | 0.007 ms, 100 ops | 0.130 ms, 24,097 ops |
| 10,000 | 0.008 ms, 100 ops | 1.384 ms, 264,721 ops |
| 100,000 | 0.012 ms, 100 ops | 5.753 ms, 2,461,432 ops |

### Workload 3: Contains Queries

| N | DynamicArray (ms, ops) | LinkedList (ms, ops) |
|---:|---:|---:|
| 100 | 0.094 ms, 5,222 ops | 0.122 ms, 10,343 ops |
| 1,000 | 0.283 ms, 49,834 ops | 0.489 ms, 99,568 ops |
| 10,000 | 1.303 ms, 482,562 ops | 1.545 ms, 965,025 ops |
| 100,000 | 2.442 ms, 4,959,713 ops | 11.350 ms, 9,919,326 ops |

### Workload 4: Removals & `extractMin`

| N | DynamicArray (ms, ops) | LinkedList (ms, ops) | MinHeap (ms, cmp) |
|---:|---:|---:|---:|
| 100 | 0.112 ms, 5,073 ops | 0.052 ms, 1,736 ops | 0.123 ms, 852 cmp |
| 1,000 | 0.139 ms, 93,670 ops | 0.052 ms, 23,996 ops | 0.054 ms, 1,769 cmp |
| 10,000 | 1.578 ms, 984,713 ops | 0.917 ms, 257,831 ops | 0.095 ms, 2,461 cmp |
| 100,000 | 2.055 ms, 9,999,846 ops | 5.602 ms, 2,487,202 ops | 0.113 ms, 3,139 cmp |

## Discussion

The Dynamic Array provides constant-time indexed access, `O(1)`, because an element's address is computed directly from its index. A linked list must follow node references to reach an indexed element, so random access is `O(N)` on average and in the worst case. The measured gap grows with `N` for that reason.

Both structures search by scanning, so `contains(x)` is `O(N)` on average and in the worst case. The Dynamic Array stores values contiguously in memory, which improves cache locality: sequential scans tend to use nearby memory and therefore perform better than following separately allocated linked-list nodes. The operation-count instrumentation also includes implementation-specific node transitions and array element accesses, so those counts are useful alongside, but are not identical to, abstract comparison counts.

A Min-Heap is designed for priority operations. Its root is the minimum, and `extractMin()` restores the heap property by moving down at most the height of the tree, giving `O(log N)` time. This makes it a natural choice when repeatedly retrieving and removing the smallest item; it is not intended for arbitrary indexed access or fast membership queries.