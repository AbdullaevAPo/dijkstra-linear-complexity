package com.github.aliabdullaev.dijkstra.common;

@FunctionalInterface
public interface DijkstraAlgoArrayBuilder {
    DijkstraAlgoArray build(int nodeId, int startLen, int nodeCnt);
}
