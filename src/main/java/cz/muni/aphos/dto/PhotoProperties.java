package cz.muni.aphos.dto;

import java.sql.Timestamp;

/**
 * The Data Transfer Object representing the photo_properties entity.
 */
public class PhotoProperties {

    private long id;
    /**
     * When the photo exposition started
     */
    private Timestamp exposureBegin;
    /**
     * When the photo exposition ended
     */
    private Timestamp exposureEnd;

    public PhotoProperties() {
    }

    public PhotoProperties(long id) {
        this.id = id;
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
