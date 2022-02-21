package com.example.astroapp.dto;

import java.sql.Timestamp;

public class FluxUserTime {

    String RA;
    String dec;
    Float apAuto;
    Double[] apertures;
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

    public Float getApAuto() {
        return apAuto;
    }

    public void setApAuto(Float apAuto) {
        this.apAuto = apAuto;
    }

    public Double[] getApertures() {
        return apertures;
    }

    public void setApertures(Double[] apertures) {
        this.apertures = apertures;
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
