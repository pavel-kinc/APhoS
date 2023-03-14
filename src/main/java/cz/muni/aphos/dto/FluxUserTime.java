package cz.muni.aphos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.aphos.helper.Night;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

/**
 * The Data Transfer Object for storing the information about a flux value, the night
 * and the time of the exposition, along with the user uploading it.
 */
//@JsonPropertyOrder({"expMiddle", "magnitude", "deviation"})
    @Schema(name="FluxData")
public class FluxUserTime {
    @JsonProperty(required = true)
    //@JsonIgnore
    String rightAsc;
    @JsonProperty(required = true)
    //@JsonIgnore
    String dec;
    @JsonProperty(required = true)
    //@JsonIgnore
    String apAuto;
    @JsonProperty(required = true)
    //@JsonIgnore
    Float apAutoDev;
    @JsonProperty(required = true)
    //@JsonIgnore
    String[] apertures;
    @JsonProperty(required = true)
    //@JsonIgnore
    Double[] apertureDevs;
    @JsonProperty(value = "cmpApAuto",required = true)
    //@JsonIgnore
    String refApAuto;
    @JsonProperty(value = "cmpApAutoDev",required = true)
    //@JsonIgnore
    Float refApAutoDev;
    @JsonProperty(value = "cmpApertures",required = true)
    //@JsonIgnore
    String[] refApertures;
    @JsonProperty(value = "cmpApertureDevs",required = true)
    //@JsonIgnore
    Double[] refApertureDevs;
    @JsonProperty(required = true)
    Float magnitude;
    @JsonProperty(required = true)
    @Nullable
    Float deviation;
    @JsonProperty(required = true)
    //@JsonIgnore
    String username;
    @JsonIgnore
    String userId;
    @JsonProperty(required = true)
    //@JsonIgnore
    Night night;
    @JsonProperty(required = true)
    String expMiddle;

    public FluxUserTime() {
    }

    /**
     * Instantiates a new FluxUserTime object.
     *
     * @param rightAsc        the right ascension of the object
     * @param dec             the declination of the object
     * @param apAuto          the ap auto of the object
     * @param apAutoDev       the ap auto deviation of the object
     * @param apertures       the apertures of the object
     * @param apertureDevs    the aperture deviations of the object
     * @param refApAuto       the ref ap auto of the reference object
     * @param refApAutoDev    the ref ap auto deviaton of the reference object
     * @param refApertures    the ref apertures of the reference object
     * @param refApertureDevs the ref aperture deviatons of the reference object
     * @param magnitude       the calculated magnitude for the chosen apertures
     * @param deviation       the calculated deviation for the chosen apertures
     * @param username        the username of the uploader
     * @param userId          the user id of the uploader
     * @param night           the night object corresponding to the flux
     * @param expMiddle       the middle timepoint of the exposition
     */
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
