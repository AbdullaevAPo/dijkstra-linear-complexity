package com.github.aliabdullaev.dijkstra.binaryheap;

import com.github.aliabdullaev.dijkstra.common.*;
import com.github.aliabdullaev.dijkstra.fibonacciheap.FibonacciHeapDijkstraAlgoQueue;

public class BinaryHeapDijkstraAlgo extends DijkstraAlgo {

    public BinaryHeapDijkstraAlgo(GraphStorage graphStorage, boolean verbose) {
        super(graphStorage, verbose);
    }

    @Override
    public DijkstraAlgoArrayBuilder getDataArrBuilder() {
        return DijkstraAlgoArray::build;
    }

    @Override
    public DijkstraAlgoQueueBuilder getQueueBuilder() {
        return BinaryHeapDijkstraAlgoQueue::build;
    }
}
