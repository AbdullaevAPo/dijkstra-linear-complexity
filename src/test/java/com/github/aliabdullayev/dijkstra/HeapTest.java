package com.github.aliabdullayev.dijkstra;

import com.github.aliabdullaev.dijkstra.binaryheap.IntBinaryHeap;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class HeapTest {
    @Test
    public void checkHeapWork() {
        IntBinaryHeap heap = new IntBinaryHeap(Integer::compareTo);
        heap.insert(10);
        heap.insert(20);
        heap.insert(5);
        heap.insert(15);
        heap.printHeapList();
        Assert.assertEquals(heap.remove(), 5);
        heap.printHeapList();
        Assert.assertEquals(heap.remove(), 10);
        heap.printHeapList();
        Assert.assertEquals(heap.remove(), 15);
        Assert.assertEquals(heap.remove(), 20);
    }

    @Test
    public void checkHeapWorkForHugeCount() {
        IntBinaryHeap heap = new IntBinaryHeap(Integer::compareTo);
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        int[] randomElements = new int[10000];
        val rand = new Random();
        for (int i = 0; i < randomElements.length; i++) {
            randomElements[i] = rand.nextInt(100);
        }
        for (int i = 0; i < 1000; i++) {
            heap.insert(randomElements[i]);
            queue.add(randomElements[i]);
        }
        for (int i = 1000; i < randomElements.length; i++) {
            heap.insert(randomElements[i]);
            queue.add(randomElements[i]);
            int minId1 = queue.remove();
            int minId2 = heap.remove();
            Assert.assertEquals(minId1, minId2);
            System.out.printf("%d, %d\n", minId1, minId2);
        }
        System.out.printf("asd");
    }
}
