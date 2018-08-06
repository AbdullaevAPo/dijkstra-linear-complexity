package com.github.aliabdullaev.dijkstra.geometry;

import lombok.Data;

import java.io.Serializable;

public @Data class GraphEdge implements Serializable {
    private GraphNode startNode;

    private GraphNode endNode;

    private int len;

    public GraphEdge(GraphNode startNode, GraphNode endNode, int len) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.len = len;
    }
}
