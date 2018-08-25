package com.github.aliabdullaev.dijkstra.fibonacciheap;

import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoArray;
import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoQueue;
import com.github.aliabdullaev.dijkstra.util.BitArray;
import lombok.val;
import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

public class FibonacciHeapDijkstraAlgoQueue extends FibonacciHeap<Integer> implements DijkstraAlgoQueue {
    private BitArray existanceArray;
    private FibonacciHeapNode<Integer>[] heapNodes;
    protected FibonacciHeapDijkstraAlgoQueue() {
        super();
    }

    public static DijkstraAlgoQueue build(DijkstraAlgoArray dArr) {
        val res = new FibonacciHeapDijkstraAlgoQueue();
        res.existanceArray = new BitArray(dArr.size());
        return res;
    }

    @Override
    public void init(int nodeId, int startLen, int nodeCnt) {
        heapNodes = new FibonacciHeapNode[nodeCnt];
        for (int i=0; i<nodeCnt; i++) {
            heapNodes[i] = new FibonacciHeapNode<>(i);
//            insert(heapNodes[i], Integer.MAX_VALUE);
        }
        insert(heapNodes[nodeId], startLen);
        existanceArray.set(nodeId);
    }

    int minId;

    @Override
    public void decreaseNodeLength(int nodeId, int newLen) {
        if (existanceArray.get(nodeId) == 0) {
            existanceArray.set(nodeId);
            insert(heapNodes[nodeId], newLen);
        } else {
            decreaseKey(heapNodes[nodeId], newLen);
        }
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public int getMinId() {
        minId = super.removeMin().getData();
        existanceArray.reset(minId);
        return minId;
    }

    @Override
    public void eraseMin() {
    }
}
