package com.example.astroapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.sql.Timestamp;

@JsonPropertyOrder({"RA", "dec", "apAuto", "refApAuto", "magnitude", "expBegin", "expEnd"})
public class FluxUserTime {

    String RA;
    String dec;
    String apAuto;
    String[] apertures;
    String refApAuto;
    String[] refApertures;
    Float magnitude;
    String username;
    String userId;
    Timestamp expBegin;
    Timestamp expEnd;

    public FluxUserTime() {
    }

    public String getRA() {
        return RA;
    }

    public void setRA(String RA) {
        this.RA = RA;
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

    public Timestamp getExpBegin() {
        return expBegin;
    }

    public void setExpBegin(Timestamp expBegin) {
        this.expBegin = expBegin;
    }

    public Timestamp getExpEnd() {
        return expEnd;
    }

    public void setExpEnd(Timestamp expEnd) {
        this.expEnd = expEnd;
    }
}
