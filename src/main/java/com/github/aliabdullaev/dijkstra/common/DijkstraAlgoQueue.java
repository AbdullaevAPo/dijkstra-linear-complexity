package com.github.aliabdullaev.dijkstra.common;

public interface DijkstraAlgoQueue {
    void init(int nodeId, int startLen, int nodeCnt);
    void decreaseNodeLength(int nodeId, int newLen);
    int size();
    int getMinId();
    void eraseMin();
}
