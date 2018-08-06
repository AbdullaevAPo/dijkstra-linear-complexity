package com.github.aliabdullaev.dijkstra.common;

import lombok.Data;
import lombok.val;

@Data
public class DijkstraAlgoArray {
    protected int[] dataArr;

    protected DijkstraAlgoArray(int size) {
        this.dataArr = new int[size];
    }

    public static DijkstraAlgoArray build(int startNodeId, int startLen, int nodeCnt) {
        val res = new DijkstraAlgoArray(nodeCnt);
        for (int i=0; i<nodeCnt; i++) {
            res.setLength(i, Integer.MAX_VALUE);
        }
        res.setLength(startNodeId, startLen);
        return res;
    }

    void setLength(int nodeId, int len) {
        dataArr[nodeId] = len;
    }

    public int getLength(int nodeId) {
        return dataArr[nodeId];
    }

    public void updateNodeInfo(int nodeId, int prevNodeId, int len) {
        setLength(nodeId, len);
    }

    public int size() {
        return dataArr.length;
    }
}
