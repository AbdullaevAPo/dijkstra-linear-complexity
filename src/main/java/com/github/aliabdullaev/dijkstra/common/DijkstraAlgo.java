package com.github.aliabdullaev.dijkstra.common;

import com.github.aliabdullaev.dijkstra.geometry.GraphNode;
import com.github.aliabdullaev.dijkstra.util.BitArray;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Collection;

import static com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils.round;

@Slf4j
public abstract class DijkstraAlgo {
    protected GraphStorage graphStorage;
    protected boolean verbose;

    public DijkstraAlgo(GraphStorage graphStorage, boolean verbose) {
        this.verbose = verbose;
        this.graphStorage = graphStorage;
    }

    public abstract DijkstraAlgoArrayBuilder getDataArrBuilder();
    public abstract DijkstraAlgoQueueBuilder getQueueBuilder();

    public DijkstraAlgoArray runDijkstra(GraphNode startPos, Collection<GraphNode> goalNodes) {
        BitArray bitArray = new BitArray(goalNodes.size());
        goalNodes.stream().mapToInt(GraphNode::getId).forEach(bitArray::set);
        return runDijkstra(startPos, bitArray);
    }

    public DijkstraAlgoArray runDijkstra(GraphNode startNode, BitArray interestedNodes) {
        int startNodeId = startNode.getId();
        DijkstraAlgoArray dArr = getDataArrBuilder().build(startNodeId, 0, graphStorage.nodes.length); // new LinearDijkstraAlgoArray(graphStorage.nodes.length);
        DijkstraAlgoQueue q = getQueueBuilder().build(dArr);
        q.init(startNodeId, 0, graphStorage.nodes.length);
        BitArray bitArray = new BitArray(interestedNodes);
        return runDijkstraAlgorithm(bitArray, dArr, q);
    }

    public DijkstraAlgoArray runAStar(GraphNode startNode, GraphNode goalNode) {
        int startNodeId = startNode.getId();
        DijkstraAlgoArray dArr = getDataArrBuilder().build(startNodeId, 0, graphStorage.nodes.length); // new LinearDijkstraAlgoArray(graphStorage.nodes.length);
        DijkstraAlgoQueue q = getQueueBuilder().build(dArr);
        q.init(startNodeId, round(graphStorage.simpleEuclideanDistance(startNode, goalNode)), graphStorage.nodes.length);
        return runAStarAlgorithm(goalNode, dArr, q);
    }

    protected DijkstraAlgoArray runDijkstraAlgorithm(
            BitArray interestedNodes, DijkstraAlgoArray dArr, DijkstraAlgoQueue q) {
        val start = System.nanoTime();
        float totalEdgeCnt = 0, setCnt = 0; // statistics
        while (q.size() != 0 && interestedNodes.getSetCnt() != 0) {
            int from = q.getAndRemoveMinId();
            interestedNodes.reset(from);

            int fromLen = dArr.getLength(from);
            int nodeOffset = graphStorage.getNodeEdgesDataOffset(from);
            int edgeCnt = graphStorage.getNodeEdgeCnt(from);
            for (int i = 0; i < edgeCnt; i++) {
                int edgeLen = graphStorage.getEdgeLen(nodeOffset, i);
                int to = graphStorage.getEndEdgeNodeId(nodeOffset, i);
                totalEdgeCnt++;
                int newToLen = fromLen + edgeLen;
                int toLen = dArr.getLength(to);
                if (newToLen > 0 && newToLen < toLen) {
                    q.decreaseNodeLength(to, newToLen);
                    dArr.updateNodeInfo(to, from, newToLen);
                }
            }
            setCnt++;
        }
        if (verbose) {
            log.info("Dijkstra call stat: {} {} {}", System.nanoTime() - start, setCnt, totalEdgeCnt);
        }
        return dArr;
    }

    /**
     * Реализация алгоритма A*
     * @return путь от startPos до goalNode
     */
    protected DijkstraAlgoArray runAStarAlgorithm (GraphNode goalNode, DijkstraAlgoArray dArr, DijkstraAlgoQueue q) {
        val start = System.nanoTime();
        int[] dataArr = dArr.getDataArr();
        float totalEdgeCnt = 0, setCnt = 0; // statistics
        while (q.size() != 0) {
            int from = q.getAndRemoveMinId();
            if (from == goalNode.getId()) {
                return dArr;
            }

            int fromLen = dArr.getLength(from);
            int nodeOffset = graphStorage.getNodeEdgesDataOffset(from);
            int edgeCnt = graphStorage.getNodeEdgeCnt(from);
            for (int i = 0; i < edgeCnt; i++) {
                int edgeLen = graphStorage.getEdgeLen(nodeOffset, i);
                int to = graphStorage.getEndEdgeNodeId(nodeOffset, i);
                totalEdgeCnt++;
                int newToLen = fromLen + edgeLen;
                int toLen = dArr.getLength(to);
                if (newToLen < toLen) {
                    toLen = newToLen;
                    q.decreaseNodeLength(to, toLen + this.heuristicLength(graphStorage.nodes[to], goalNode));
                    dArr.updateNodeInfo(to, from, toLen);
                }
            }
            setCnt++;
        }
        log.info("ВЫЗОВ ДЕЙКСТРЫ 2: {} {} {}", System.nanoTime() - start, setCnt, totalEdgeCnt);
        return dArr;
    }

    protected int heuristicLength(GraphNode startNode, GraphNode endNode) {
        return (int)(graphStorage.simpleEuclideanDistance(startNode, endNode));
    }

}