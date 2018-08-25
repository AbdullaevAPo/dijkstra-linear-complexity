package com.github.aliabdullayev.dijkstra;

import com.github.aliabdullaev.dijkstra.binaryheap.BinaryHeapDijkstraAlgo;
import com.github.aliabdullaev.dijkstra.binaryheap.BinaryHeapDijkstraAlgoQueue;
import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoArray;
import com.github.aliabdullaev.dijkstra.common.GraphStorage;
import com.github.aliabdullaev.dijkstra.fibonacciheap.FibonacciHeapDijkstraAlgo;
import com.github.aliabdullaev.dijkstra.geometry.GraphEdge;
import com.github.aliabdullaev.dijkstra.geometry.GraphNode;
import com.github.aliabdullaev.dijkstra.linear.LinearDijkstraAlgo;
import com.github.aliabdullaev.dijkstra.util.BitArray;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils.round;
import static java.lang.Math.abs;

@Slf4j
public class PerformanceTest {
    int randomSeed = 0;
    Random rand = new Random(randomSeed);

    @Test
    public void test() {
        int nodeCnt = 1000;
        while (nodeCnt < 1_000_000) {
            GraphStorage graph = generateGraph(nodeCnt, 10, 30);
            double linearTime = 0;
            double fibonacciTime = 0;
            double heapTime = 0;
            for (int x = 0; x < 100; x++) {
                GraphNode startNode = graph.nodes[rand.nextInt(graph.nodes.length)];
                BitArray targetNodes = new BitArray(Arrays.stream(graph.nodes).map(GraphNode::getId).collect(Collectors.toSet()));

                long startTime = System.nanoTime();
                val dArr1 = new LinearDijkstraAlgo(graph, true).runDijkstra(startNode, targetNodes);
                linearTime += (System.nanoTime() - startTime) / 1.0E9;
                log.info("{}", Arrays.stream(graph.nodes).filter(i -> dArr1.getLength(i.getId()) < Integer.MAX_VALUE).count());

                startTime = System.nanoTime();
                val dArr2 = new FibonacciHeapDijkstraAlgo(graph, true).runDijkstra(startNode, targetNodes);
                fibonacciTime += (System.nanoTime() - startTime) / 1.0E9;
                qualityCheck(dArr1, dArr2);

                startTime = System.nanoTime();
                val dArr3 = new BinaryHeapDijkstraAlgo(graph, true).runDijkstra(startNode, targetNodes);
                heapTime += (System.nanoTime() - startTime) / 1.0E9;
                qualityCheck(dArr2, dArr3);
            }
            nodeCnt = (int) (nodeCnt * 1.5);
            log.info("nodeCnt: {}, edgeCnt: {}, linearTime: {}, fibonacciTime: {}, binaryHeapTime: {}", nodeCnt, graph.getTotalEdgeCnt(),
                    String.format("%3f", linearTime), String.format("%3f", fibonacciTime), String.format("%3f", heapTime));
        }
    }

    private GraphStorage generateGraph(int nodeCnt, int medianEdgeCnt, int medianEdgeLen) {
        val nodes = IntStream.range(0, nodeCnt).mapToObj(i -> new GraphNode(i, rand.nextLong(), rand.nextFloat(), rand.nextFloat())).toArray(GraphNode[]::new);
        for (GraphNode node : nodes) {
            val edgeCnt = abs(round(rand.nextGaussian() * medianEdgeCnt)) + 1;
            if (edgeCnt == 0) {
                log.info("");
            }
            for (int i = 0; i < edgeCnt; i++) {
                val targetNode = nodes[getTargetNodeId(nodeCnt, node)];
                val edgeLen = getEdgeLen(medianEdgeLen, 20);
                val edge = new GraphEdge(node, targetNode, edgeLen);
                val revertedEdge = new GraphEdge(targetNode, node, edgeLen);
                targetNode.getIncomingEdges().put(node.getOrigId(), edge);
                node.getOutcomingEdges().put(targetNode.getOrigId(), edge);
                targetNode.getOutcomingEdges().put(node.getOrigId(), revertedEdge);
                node.getIncomingEdges().put(targetNode.getOrigId(), revertedEdge);
            }
        }
        log.info("Median edge len: {}", Arrays.stream(nodes).flatMap(i -> i.getOutcomingEdges().values().stream())
                .mapToDouble(i -> i.getLen())
                .average().orElse(0.0)
        );
        Map<Integer, Long> edgeLenHist = Arrays.stream(nodes).flatMap(i -> i.getOutcomingEdges().values().stream())
                .map(i -> round(i.getLen()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        log.info("Hist of edge len: {}", edgeLenHist);
        return new GraphStorage(nodes);
    }

    private int getTargetNodeId(int nodeCnt, GraphNode startNode) {
        int startNodeId = startNode.getId();
        int targetNodeId = startNodeId;
        while (targetNodeId == startNodeId || isExistRelationWithTarget(startNode, targetNodeId)) {
            targetNodeId = rand.nextInt(nodeCnt);
        }
        return targetNodeId;
    }

    private boolean isExistRelationWithTarget(GraphNode graphNode, final int targetNode) {
        return graphNode.getOutcomingEdges().values().stream().anyMatch(i -> i.getEndNode().getId() == targetNode);
    }

    private void qualityCheck(DijkstraAlgoArray dArr1, DijkstraAlgoArray dArr2) {
        for (int i = 0; i < dArr1.size(); i++) {
            Assert.assertEquals(dArr1.getLength(i), dArr2.getLength(i));
        }
    }

//    private String printJson(GraphStorage graph) {
//        Map<Integer, Long> countOfEdgeLen  = Arrays.stream(graph.nodes).flatMap(i -> i.getOutcomingEdges().values().stream())
//                .map(i -> round(i.getLen()))
//                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//        countOfEdgeLen.entrySet().stream().reduce("", (str, k) -> str + ("" + k.getKey() + ":" + k.getValue()))
//    }

    private int getEdgeLen(int minLen, int medianEdgeLen) {
        return abs(round(rand.nextGaussian() * medianEdgeLen)) + minLen;
    }
}
