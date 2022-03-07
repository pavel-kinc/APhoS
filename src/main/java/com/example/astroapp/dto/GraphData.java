package com.example.astroapp.dto;

public class GraphData {

    String time;
    Float magnitude;

    public GraphData(String expBegin, Float magnitude) {
        this.time = expBegin;
        this.magnitude = magnitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Float magnitude) {
        this.magnitude = magnitude;
    }
}
