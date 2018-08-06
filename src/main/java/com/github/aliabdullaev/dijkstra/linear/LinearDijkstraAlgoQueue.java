package com.github.aliabdullaev.dijkstra.linear;

import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoArray;
import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoQueue;

public class LinearDijkstraAlgoQueue extends SortedHashMap implements DijkstraAlgoQueue {

    protected LinearDijkstraAlgoQueue(LinearDijkstraAlgoArray dArr) {
        super(dArr);
    }

    public static DijkstraAlgoQueue build(DijkstraAlgoArray dArr) {
        if (!(dArr instanceof LinearDijkstraAlgoArray)) {
            throw new IllegalArgumentException("");
        }
        return new LinearDijkstraAlgoQueue((LinearDijkstraAlgoArray) dArr);
    }

    @Override
    public void init(int nodeId, int startLen, int nodeCnt) {
        init(nodeId, startLen);
    }

    @Override
    public void decreaseNodeLength(int nodeId, int newLen) {
        erase(nodeId);
        insert(nodeId, newLen);
    }

    int minId;

    @Override
    public int getMinId() {
        minId = super.getMinId();
        return minId;
    }

    @Override
    public void eraseMin() {
        erase(minId);
    }
}
