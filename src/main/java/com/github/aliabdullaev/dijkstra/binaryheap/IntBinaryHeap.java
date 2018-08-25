package com.github.aliabdullaev.dijkstra.binaryheap;

import com.github.aliabdullaev.dijkstra.util.IntComparator;
import com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils;
import lombok.Data;

import java.util.Comparator;

import static com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils.getMin;

public class IntBinaryHeap {
    protected int[] data;
    int size = 0;
    IntComparator comparator;
    private static final int DEFAULT_SIZE = 100;

    public IntBinaryHeap(Comparator<Integer> comparator) {
        this(DEFAULT_SIZE, comparator::compare);
    }

    public IntBinaryHeap(int capacity, IntComparator comparator) {
        data = new int[capacity];
        for (int i=0; i<data.length; i++) {
            data[i] = Integer.MAX_VALUE;
        }
        this.comparator = comparator;
    }

    protected IntBinaryHeap(int[] data, int size, IntComparator comparator) {
        this.data = data;
        this.comparator = comparator;
        this.size = size;
    }

    public int insert(int value) {
        size++;
        if (size >= data.length) {
            int newLength = (int) (data.length * 1.5) + 1;
            int[] copy = new int[newLength];
            System.arraycopy(data, 0, copy, 0,
                    data.length);
            data = copy;
        }
        this.data[size - 1] = value;
        return this.bubbleUp(size - 1);
    }

    protected int bubbleUp(int index) {
        // TASK: Find the parent index
        int parentIndex = (index - 1) / 2;

        // TASK: check halt condition, if parent is less than children
        // or current is the root, we need to do nothing
        while (comparator.compare(data[index], data[parentIndex]) < 0) {
            // TASK: Swap children and parent
            int parentValue = this.data[parentIndex];
            this.data[parentIndex] = this.data[index];
            this.data[index] = parentValue;

            // TASK: Recursively do the same thing with new parent
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
        return index;
    }

    public int remove() {
        int minValue = this.data[0];
        int lastValue = this.data[size - 1];
        this.data[0] = lastValue;
        this.sinkDown(0);
        size--;
        return minValue;
    }

    protected boolean isLeaf(int index) {
        return index * 2 + 1 >= size;
    }

    protected int sinkDown(int index) {
        while (!this.isLeaf(index)) {
            int leftChildIndex = getMin(size, index * 2 + 1);
            int rightChildIndex = getMin(size, index * 2 + 2);
            int minChildIndex = leftChildIndex;
            if (rightChildIndex < size && comparator.compare(data[rightChildIndex], data[leftChildIndex]) < 0) {
                minChildIndex = rightChildIndex;
            }
            if (comparator.compare(data[index], data[minChildIndex]) < 0) {
                break;
            }
            int childValue = this.data[minChildIndex];
            this.data[minChildIndex] = this.data[index];
            this.data[index] = childValue;
            index = minChildIndex;
        }
        return index;
    }

    public int minValue() {
        return this.data[0];
    }

    public void printHeapList() {
        for (int i = 0; i < size; i++) {
            System.out.printf("%d, ", this.data[i]);
        }
        System.out.println("");
    }

    public int size() {
        return size;
    }

}
