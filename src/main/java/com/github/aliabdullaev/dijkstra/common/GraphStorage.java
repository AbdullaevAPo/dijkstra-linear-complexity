package com.github.aliabdullaev.dijkstra.common;

import com.github.aliabdullaev.dijkstra.geometry.GeoPoint;
import com.github.aliabdullaev.dijkstra.geometry.GraphEdge;
import com.github.aliabdullaev.dijkstra.geometry.GraphNode;
import com.github.aliabdullaev.dijkstra.util.performance.MathUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class GraphStorage {
    public GraphNode[] nodes;
    public Map<Long, GraphNode> nodeIdMap = new HashMap<>();
    // records represented as struct { <- TODO
    //  int node_id;
    //  int edge_cnt;
    //      arr_below ->
    //  int edge_end_node;
    //  int edge_len;
    // }
    private int[] offsetArr;
    private int[] outcomingEdgesDataArr;

    public GraphStorage(GraphNode[]  nodes) {
        this.nodes = nodes;
        this.nodeIdMap = Arrays.stream(nodes).collect(Collectors.toMap(GraphNode::getOrigId, i -> i));
        initDataAfterLoadingFromDB();
    }


    public void initDataAfterLoadingFromDB() {
        initHighPerformanceStructures();
    }

    public void initHighPerformanceStructures() {
        List<GraphEdge> allEdges = Arrays.stream(nodes).map(GraphNode::getOutcomingEdges).map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
        outcomingEdgesDataArr = new int[allEdges.size() * 3];
        offsetArr = new int[nodes.length * 2];
        int i = 0;
        for (GraphNode node : nodes) {
            offsetArr[node.getId() * 2] = i;
            offsetArr[node.getId() * 2 + 1] = node.getOutcomingEdges().size();
            for (GraphEdge edge : node.getOutcomingEdges().values()) {
                outcomingEdgesDataArr[i++] = edge.getEndNode().getId();
                outcomingEdgesDataArr[i++] = edge.getLen();
            }
        }
    }

    public int getTotalEdgeCnt() {
        return Arrays.stream(nodes).mapToInt(i -> i.getOutcomingEdges().size()).sum();
    }

    public int getNodeEdgeCnt(int nodeId) {
        return offsetArr[nodeId * 2 + 1];
    }

    public int getNodeEdgesDataOffset(int nodeId) {
        return offsetArr[nodeId * 2];
    }

    public int getEndEdgeNodeId(int edgeDataOffset, int edgePos) {
        return outcomingEdgesDataArr[edgeDataOffset + edgePos * 2];
    }

    public int getEdgeLen(int edgeDataOffset, int edgePos) {
        return outcomingEdgesDataArr[edgeDataOffset + edgePos * 2 + 1];
    }

    public float simpleEuclideanDistance(GraphNode g1, GraphNode g2) {
        double latKm = 1/(g1.lat - g2.lat);
        double lonKm = 1/(g2.lon - g1.lon);
        return (float) MathUtils.sqrt(latKm * latKm + lonKm * lonKm) * 1E6f;
    }
}
