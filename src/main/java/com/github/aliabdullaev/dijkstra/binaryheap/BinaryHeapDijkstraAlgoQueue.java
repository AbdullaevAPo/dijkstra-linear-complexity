package com.github.aliabdullaev.dijkstra.binaryheap;

import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoArray;
import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoQueue;
import com.github.aliabdullaev.dijkstra.fibonacciheap.FibonacciHeapDijkstraAlgoQueue;
import com.github.aliabdullaev.dijkstra.util.BitArray;
import lombok.val;
import org.jgrapht.util.FibonacciHeap;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BinaryHeapDijkstraAlgoQueue extends IntBinaryHeap implements DijkstraAlgoQueue {
    private BitArray existanceArray;
    private int[] posOfNodesInHeap;

    public BinaryHeapDijkstraAlgoQueue(DijkstraAlgoArray dArr) {
        super(dArr.size(), dArr::compareByLength);
        existanceArray = new BitArray(dArr.size());
        posOfNodesInHeap = new int[dArr.size()];
    }

    public static DijkstraAlgoQueue build(DijkstraAlgoArray dArr) {
        return new BinaryHeapDijkstraAlgoQueue(dArr);
    }

    @Override
    public void init(int nodeId, int startLen, int nodeCnt) {
        decreaseNodeLength(nodeId, startLen);
    }

    @Override
    public void decreaseNodeLength(int nodeId, int newLen) {
        if (existanceArray.get(nodeId) == 0) {
            existanceArray.set(nodeId);
            posOfNodesInHeap[nodeId] = insert(nodeId);
        } else {
            posOfNodesInHeap[nodeId] = sinkDown(posOfNodesInHeap[nodeId]);
        }
    }

    @Override
    public int getMinId() {
        int minId = remove();
        existanceArray.reset(minId);
        return minId;
    }

    @Override
    public void eraseMin() {
    }
}
