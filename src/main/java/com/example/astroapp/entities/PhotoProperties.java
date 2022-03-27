package com.example.astroapp.entities;

import java.sql.Timestamp;

public class PhotoProperties {
    private long id;
    private Timestamp exposureBegin;
    private Timestamp exposureEnd;

    public PhotoProperties() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getExposureBegin() {
        return exposureBegin;
    }

    public void setExposureBegin(Timestamp exposureBegin) {
        this.exposureBegin = exposureBegin;
    }

    public Timestamp getExposureEnd() {
        return exposureEnd;
    }

    public void setExposureEnd(Timestamp exposureEnd) {
        this.exposureEnd = exposureEnd;
    }
}
