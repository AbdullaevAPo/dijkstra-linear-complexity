package com.github.aliabdullaev.dijkstra.geometry;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
public @Data class GeoPoint implements Serializable {

    public float lat;
    public float lon;

    public GeoPoint(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
