package com.example.astroapp.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class PhotoProperties {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "photo_prop_generator")
    @SequenceGenerator(name = "photo_prop_generator", sequenceName = "photo_properties_id_seq", allocationSize = 1)
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
