package com.example.astroapp.dto;

import com.example.astroapp.helper.Night;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"rightAsc", "dec", "magnitude", "expBegin", "expEnd"})
public class FluxUserTime {
    String rightAsc;
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
    String username;
    @JsonIgnore
    String userId;
    @JsonIgnore
    Night night;
    String expBegin;
    String expEnd;

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

    public String getExpBegin() {
        return expBegin;
    }

    public void setExpBegin(String expBegin) {
        this.expBegin = expBegin;
    }

    public String getExpEnd() {
        return expEnd;
    }

    public void setExpEnd(String expEnd) {
        this.expEnd = expEnd;
    }

    public Night getNight() {
        return night;
    }

    public void setNight(Night night) {
        this.night = night;
    }
}
