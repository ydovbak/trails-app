package com.dovbak.trails.models;

import java.util.ArrayList;
import java.util.Arrays;

public class TrailModel {
    private String name;
    private String description;
    private ArrayList<TrailPoint> points;
    private String userId;

    public TrailModel(String name, String description, ArrayList<TrailPoint> points) {
        this.name = name;
        this.description = description;
        this.points = points;
    }

    public TrailModel(String name, String description,  TrailPoint[]points) {
        this.name = name;
        this.description = description;
        this.points.addAll(Arrays.asList(points));
    }

    public TrailModel() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<TrailPoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<TrailPoint> points) {
        this.points = points;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
