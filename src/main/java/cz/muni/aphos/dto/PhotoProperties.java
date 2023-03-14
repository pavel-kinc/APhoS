package cz.muni.aphos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import cz.muni.aphos.helper.ViewField;

import java.sql.Timestamp;

/**
 * The Data Transfer Object representing the photo_properties entity.
 */
public class PhotoProperties {

    @JsonIgnore
    private long id;
    /**
     * When the photo exposition started
     */
    @JsonView(ViewField.Public.class)
    @JsonProperty(required = true)
    private Timestamp exposureBegin;
    /**
     * When the photo exposition ended
     */
    @JsonView(ViewField.Public.class)
    @JsonProperty(required = true)
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
