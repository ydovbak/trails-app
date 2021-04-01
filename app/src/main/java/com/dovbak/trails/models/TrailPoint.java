package com.dovbak.trails.models;

import com.google.android.gms.maps.model.LatLng;

public class TrailPoint {

    private String name;
    private String lat;
    private String lng;
    private int order;

    public TrailPoint(String name, String lat, String lng, int order) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.order = order;
    }

    public TrailPoint() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public LatLng toLatLng() {
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
    }

}
