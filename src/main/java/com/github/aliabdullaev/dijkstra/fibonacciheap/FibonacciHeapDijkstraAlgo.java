package com.github.aliabdullaev.dijkstra.fibonacciheap;

import com.github.aliabdullaev.dijkstra.common.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FibonacciHeapDijkstraAlgo extends DijkstraAlgo {

    public FibonacciHeapDijkstraAlgo(GraphStorage graphStorage, boolean verbose) {
        super(graphStorage, verbose);
    }

    @Override
    public DijkstraAlgoArrayBuilder getDataArrBuilder() {
        return DijkstraAlgoArray::build;
    }

    @Override
    public DijkstraAlgoQueueBuilder getQueueBuilder() {
        return FibonacciHeapDijkstraAlgoQueue::build;
    }
}
