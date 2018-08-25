package com.github.aliabdullaev.dijkstra.common;

import java.util.Map;
import java.util.SortedMap;

public class BucketSizeOptimizer {
    private GraphStorage graphStorage;

    public int findOptimumBucketSize() {
        SortedMap<Integer, Long> hist = graphStorage.getEdgeLenHist();
        // get initial point of optimization
        double defaultPercentile = 0.1;
        long totalEdgeCnt = hist.values().stream().mapToLong(i -> i).sum();
        long curCnt = 0;
        for (Map.Entry<Integer, Long> entry: hist.entrySet()) {
            int edgeLen = entry.getKey();
            long cntWithEdgeLen = entry.getValue();
            curCnt += cntWithEdgeLen;
            if (curCnt < totalEdgeCnt * defaultPercentile) {
                return edgeLen;
            }
        }
        return hist.lastKey();
    }

//    private int get initial
}
