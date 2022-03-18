package com.example.astroapp.dto;

import com.example.astroapp.helper.Night;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"expMiddle", "magnitude"})
public class FluxUserTime {
    @JsonIgnore
    String rightAsc;
    @JsonIgnore
    String dec;
    @JsonIgnore
    String apAuto;
    @JsonIgnore
    String[] apertures;
    @JsonIgnore
    String refApAuto;
    @JsonIgnore
    String[] refApertures;
    Float magnitude;
    Float errorTop;
    Float errorBottom;
    @JsonIgnore
    String username;
    @JsonIgnore
    String userId;
    @JsonIgnore
    Night night;
    String expMiddle;

    public FluxUserTime() {
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

    public Float getErrorTop() {
        return errorTop;
    }

    public void setErrorTop(Float errorTop) {
        this.errorTop = errorTop;
    }

    public Float getErrorBottom() {
        return errorBottom;
    }

    public void setErrorBottom(Float errorBottom) {
        this.errorBottom = errorBottom;
    }
}
