package com.github.aliabdullaev.dijkstra.linear;

import com.github.aliabdullaev.dijkstra.common.DijkstraAlgoArray;
import lombok.Data;
import lombok.val;

@Data
public class LinearDijkstraAlgoArray extends DijkstraAlgoArray {
    public static int DIJKSTRA_ENTRY_FIELDS_CNT = 4;

    public LinearDijkstraAlgoArray(int size) {
        super(size * DIJKSTRA_ENTRY_FIELDS_CNT);
        for (int i=0; i<size; i++) {
            updateNodeInfo(i, Integer.MAX_VALUE, -1, -1, -1);
        }
    }

    public int size() {
        return dataArr.length / DIJKSTRA_ENTRY_FIELDS_CNT;
    }

    void setLength(int nodeId, int len) {
        dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT] = len;
    }

    public int getLength(int nodeId) {
        return dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT];
    }

    void setPrevNodeId(int nodeId, int prevNodeId) {
        dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT + 1] = prevNodeId;
    }

    public int getPrevNodeId(int nodeId) {
        return dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT + 1];
    }

    protected void setBucketNum(int nodeId, int bucketNum) {
        dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT + 2] = bucketNum;
    }

    public int getBucketNum(int nodeId) {
        return dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT + 2];
    }

    protected void setPosInBucket(int nodeId, int posInBucket) {
        dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT + 3] = posInBucket;
    }

    public int getPosInBucket(int nodeId) {
        return dataArr[nodeId * DIJKSTRA_ENTRY_FIELDS_CNT + 3];
    }

    public void updateNodeInfo(int nodeId, int... updateInfo) {
        setLength(nodeId, updateInfo[0]);
        setPrevNodeId(nodeId, updateInfo[1]);
        setBucketNum(nodeId, updateInfo[2]);
        setPosInBucket(nodeId, updateInfo[3]);
    }

    public void updateNodeInfo(int nodeId, int prevNodeId, int len) {
        setLength(nodeId, len);
        setPrevNodeId(nodeId, prevNodeId);
    }

    public static DijkstraAlgoArray build(int startNodeId, int startLen, int nodeCnt) {
        val res = new LinearDijkstraAlgoArray(nodeCnt);
        for (int i=0; i<nodeCnt; i++) {
            res.updateNodeInfo(startNodeId, Integer.MAX_VALUE, -1, -1, -1);
        }
        res.updateNodeInfo(startNodeId, startLen, -1, -1, -1);
        return res;
    }
}
