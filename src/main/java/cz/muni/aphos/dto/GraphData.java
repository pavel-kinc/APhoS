package cz.muni.aphos.dto;

/**
 * The Data Transfer Object for x and y values of one point on the light curve graph.
 */
public class GraphData {


    String time;
    Float magnitude;

    /**
     * Instantiates a new Graph data.
     *
     * @param expMiddle the timepoint in the middle of the exposition
     * @param magnitude the magnitude measured for the exposition
     */
    public GraphData(String expMiddle, Float magnitude) {
        this.time = expMiddle;
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
