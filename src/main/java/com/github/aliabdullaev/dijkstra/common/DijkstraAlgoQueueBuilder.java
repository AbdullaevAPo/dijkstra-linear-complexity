package com.github.aliabdullaev.dijkstra.common;

@FunctionalInterface
public interface DijkstraAlgoQueueBuilder {
    DijkstraAlgoQueue build(DijkstraAlgoArray dijkstraAlgoQueue);
}
