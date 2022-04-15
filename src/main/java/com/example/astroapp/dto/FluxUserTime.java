package com.example.astroapp.dto;

import com.example.astroapp.helper.Night;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Data Transfer Object for storing the information about a flux value, the night
 * and the time of the exposition, along with the user uploading it.
 */
@JsonPropertyOrder({"expMiddle", "magnitude", "deviation"})
public class FluxUserTime {
    @JsonIgnore
    String rightAsc;
    @JsonIgnore
    String dec;
    @JsonIgnore
    String apAuto;
    @JsonIgnore
    Float apAutoDev;
    @JsonIgnore
    String[] apertures;
    @JsonIgnore
    Double[] apertureDevs;
    @JsonIgnore
    String refApAuto;
    @JsonIgnore
    Float refApAutoDev;
    @JsonIgnore
    String[] refApertures;
    @JsonIgnore
    Double[] refApertureDevs;
    Float magnitude;
    Float deviation;
    @JsonIgnore
    String username;
    @JsonIgnore
    String userId;
    @JsonIgnore
    Night night;
    String expMiddle;

    public FluxUserTime() {
    }

    public FluxUserTime(String rightAsc, String dec, String apAuto, Float apAutoDev,
                        String[] apertures, Double[] apertureDevs, String refApAuto, Float refApAutoDev,
                        String[] refApertures, Double[] refApertureDevs, Float magnitude, Float deviation,
                        String username, String userId, Night night, String expMiddle) {
        this.rightAsc = rightAsc;
        this.dec = dec;
        this.apAuto = apAuto;
        this.apAutoDev = apAutoDev;
        this.apertures = apertures;
        this.apertureDevs = apertureDevs;
        this.refApAuto = refApAuto;
        this.refApAutoDev = refApAutoDev;
        this.refApertures = refApertures;
        this.refApertureDevs = refApertureDevs;
        this.magnitude = magnitude;
        this.deviation = deviation;
        this.username = username;
        this.userId = userId;
        this.night = night;
        this.expMiddle = expMiddle;
    }

    public String getRightAsc() {
        return rightAsc;
    }

    public void setRightAsc(String rightAsc) {
        this.rightAsc = rightAsc;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getApAuto() {
        return apAuto;
    }

    public void setApAuto(String apAuto) {
        this.apAuto = apAuto;
    }

    public String[] getApertures() {
        return apertures;
    }

    public void setApertures(String[] apertures) {
        this.apertures = apertures;
    }

    public String getRefApAuto() {
        return refApAuto;
    }

    public void setRefApAuto(String refApAuto) {
        this.refApAuto = refApAuto;
    }

    public String[] getRefApertures() {
        return refApertures;
    }

    public void setRefApertures(String[] refApertures) {
        this.refApertures = refApertures;
    }

    public Float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Float magnitude) {
        this.magnitude = magnitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpMiddle() {
        return expMiddle;
    }

    public void setExpMiddle(String expMiddle) {
        this.expMiddle = expMiddle;
    }

    public Night getNight() {
        return night;
    }

    public void setNight(Night night) {
        this.night = night;
    }

    public Float getDeviation() {
        return deviation;
    }

    public void setDeviation(Float deviation) {
        this.deviation = deviation;
    }

    public Float getApAutoDev() {
        return apAutoDev;
    }

    public void setApAutoDev(Float apAutoDev) {
        this.apAutoDev = apAutoDev;
    }

    public Double[] getApertureDevs() {
        return apertureDevs;
    }

    public void setApertureDevs(Double[] apertureDevs) {
        this.apertureDevs = apertureDevs;
    }

    public double getRefApAutoDev() {
        return refApAutoDev;
    }

    public void setRefApAutoDev(Float refApAutoDev) {
        this.refApAutoDev = refApAutoDev;
    }

    public Double[] getRefApertureDevs() {
        return refApertureDevs;
    }

    public void setRefApertureDevs(Double[] refApertureDevs) {
        this.refApertureDevs = refApertureDevs;
    }
}
