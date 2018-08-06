package com.github.aliabdullaev.dijkstra.geometry;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public @Data class GraphNode extends GeoPoint {

    public static int idCounter = 0;
    private int id;
    private long origId;

    private Map<Long, GraphEdge> outcomingEdges = new HashMap<>();
    private Map<Long, GraphEdge> incomingEdges = new HashMap<>();

    public GraphNode(long origId, float lat, float lon) {
        this(idCounter++, origId, lat, lon);
    }

    public GraphNode(int id, long origId, float lat, float lon) {
        super(lat, lon);
        this.origId = origId;
        this.id = id;
    }


    public GeoPoint buildGeoPoint() {
        return new GeoPoint(lat, lon);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
