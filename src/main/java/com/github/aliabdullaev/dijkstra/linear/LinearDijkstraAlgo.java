package com.github.aliabdullaev.dijkstra.linear;

import com.github.aliabdullaev.dijkstra.common.*;
import com.github.aliabdullaev.dijkstra.geometry.GraphNode;
import com.github.aliabdullaev.dijkstra.util.BitArray;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static com.github.aliabdullaev.dijkstra.linear.LinearDijkstraAlgoArray.*;

@Slf4j
public class LinearDijkstraAlgo extends DijkstraAlgo {

    public LinearDijkstraAlgo(GraphStorage graphStorage, boolean verbose) {
        super(graphStorage, verbose);
    }

    @Override
    public DijkstraAlgoArrayBuilder getDataArrBuilder() {
        return LinearDijkstraAlgoArray::build;
    }

    @Override
    public DijkstraAlgoQueueBuilder getQueueBuilder() {
        return LinearDijkstraAlgoQueue::build;
    }
}
