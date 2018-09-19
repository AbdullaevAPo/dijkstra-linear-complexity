package com.github.aliabdullaev.dijkstra.binaryheap;

import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoArray;
import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoQueue;
import com.github.aliabdullaev.dijkstra.util.BitArray;

public class BinaryHeapDijkstraAlgoQueue extends IntBinaryHeap implements DijkstraAlgoQueue {
    private BitArray existenceArray;
    private int[] posOfNodesInHeap;

    public BinaryHeapDijkstraAlgoQueue(DijkstraAlgoArray dArr) {
        super(dArr.size(), dArr::compareByLength);
        existenceArray = new BitArray(dArr.size());
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
        if (existenceArray.get(nodeId) == 0) {
            existenceArray.set(nodeId);
            posOfNodesInHeap[nodeId] = insert(nodeId);
        } else {
            posOfNodesInHeap[nodeId] = sinkDown(posOfNodesInHeap[nodeId]);
        }
    }

    @Override
    public int getAndRemoveMinId() {
        int minId = remove();
        existenceArray.reset(minId);
        return minId;
    }
}
