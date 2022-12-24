package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.PhotoProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Flux
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-23T18:56:03.993Z[GMT]")


public class Flux   {
  @JsonProperty("rightAsc")
  private String rightAsc = null;

  @JsonProperty("declination")
  private String declination = null;

  @JsonProperty("magnitude")
  private Float magnitude = null;

  @JsonProperty("expMiddle")
  private String expMiddle = null;

  @JsonProperty("deviation")
  private Float deviation = null;

  @JsonProperty("addedBy")
  private String addedBy = null;

  @JsonProperty("apAuto")
  private Double apAuto = null;

  @JsonProperty("apertures")
  @Valid
  private List<Double> apertures = null;

  @JsonProperty("apAutoCmp")
  private Double apAutoCmp = null;

  @JsonProperty("aperturesCmp")
  @Valid
  private List<Double> aperturesCmp = null;

  @JsonProperty("photo")
  private PhotoProperties photo = null;

  public Flux rightAsc(String rightAsc) {
    this.rightAsc = rightAsc;
    return this;
  }

  /**
   * Get rightAsc
   * @return rightAsc
   **/
  @Schema(description = "")
  
    public String getRightAsc() {
    return rightAsc;
  }

  public void setRightAsc(String rightAsc) {
    this.rightAsc = rightAsc;
  }

  public Flux declination(String declination) {
    this.declination = declination;
    return this;
  }

  /**
   * Get declination
   * @return declination
   **/
  @Schema(description = "")
  
    public String getDeclination() {
    return declination;
  }

  public void setDeclination(String declination) {
    this.declination = declination;
  }

  public Flux magnitude(Float magnitude) {
    this.magnitude = magnitude;
    return this;
  }

  /**
   * Get magnitude
   * @return magnitude
   **/
  @Schema(description = "")
  
    public Float getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(Float magnitude) {
    this.magnitude = magnitude;
  }

  public Flux expMiddle(String expMiddle) {
    this.expMiddle = expMiddle;
    return this;
  }

  /**
   * Get expMiddle
   * @return expMiddle
   **/
  @Schema(description = "")
  
    public String getExpMiddle() {
    return expMiddle;
  }

  public void setExpMiddle(String expMiddle) {
    this.expMiddle = expMiddle;
  }

  public Flux deviation(Float deviation) {
    this.deviation = deviation;
    return this;
  }

  /**
   * Get deviation
   * @return deviation
   **/
  @Schema(description = "")
  
    public Float getDeviation() {
    return deviation;
  }

  public void setDeviation(Float deviation) {
    this.deviation = deviation;
  }

  public Flux addedBy(String addedBy) {
    this.addedBy = addedBy;
    return this;
  }

  /**
   * Get addedBy
   * @return addedBy
   **/
  @Schema(description = "")
  
    public String getAddedBy() {
    return addedBy;
  }

  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }

  public Flux apAuto(Double apAuto) {
    this.apAuto = apAuto;
    return this;
  }

  /**
   * Get apAuto
   * @return apAuto
   **/
  @Schema(description = "")
  
    public Double getApAuto() {
    return apAuto;
  }

  public void setApAuto(Double apAuto) {
    this.apAuto = apAuto;
  }

  public Flux apertures(List<Double> apertures) {
    this.apertures = apertures;
    return this;
  }

  public Flux addAperturesItem(Double aperturesItem) {
    if (this.apertures == null) {
      this.apertures = new ArrayList<Double>();
    }
    this.apertures.add(aperturesItem);
    return this;
  }

  /**
   * Get apertures
   * @return apertures
   **/
  @Schema(description = "")
  
    public List<Double> getApertures() {
    return apertures;
  }

  public void setApertures(List<Double> apertures) {
    this.apertures = apertures;
  }

  public Flux apAutoCmp(Double apAutoCmp) {
    this.apAutoCmp = apAutoCmp;
    return this;
  }

  /**
   * Get apAutoCmp
   * @return apAutoCmp
   **/
  @Schema(description = "")
  
    public Double getApAutoCmp() {
    return apAutoCmp;
  }

  public void setApAutoCmp(Double apAutoCmp) {
    this.apAutoCmp = apAutoCmp;
  }

  public Flux aperturesCmp(List<Double> aperturesCmp) {
    this.aperturesCmp = aperturesCmp;
    return this;
  }

  public Flux addAperturesCmpItem(Double aperturesCmpItem) {
    if (this.aperturesCmp == null) {
      this.aperturesCmp = new ArrayList<Double>();
    }
    this.aperturesCmp.add(aperturesCmpItem);
    return this;
  }

  /**
   * Get aperturesCmp
   * @return aperturesCmp
   **/
  @Schema(description = "")
  
    public List<Double> getAperturesCmp() {
    return aperturesCmp;
  }

  public void setAperturesCmp(List<Double> aperturesCmp) {
    this.aperturesCmp = aperturesCmp;
  }

  public Flux photo(PhotoProperties photo) {
    this.photo = photo;
    return this;
  }

  /**
   * Get photo
   * @return photo
   **/
  @Schema(description = "")
  
    @Valid
    public PhotoProperties getPhoto() {
    return photo;
  }

  public void setPhoto(PhotoProperties photo) {
    this.photo = photo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Flux flux = (Flux) o;
    return Objects.equals(this.rightAsc, flux.rightAsc) &&
        Objects.equals(this.declination, flux.declination) &&
        Objects.equals(this.magnitude, flux.magnitude) &&
        Objects.equals(this.expMiddle, flux.expMiddle) &&
        Objects.equals(this.deviation, flux.deviation) &&
        Objects.equals(this.addedBy, flux.addedBy) &&
        Objects.equals(this.apAuto, flux.apAuto) &&
        Objects.equals(this.apertures, flux.apertures) &&
        Objects.equals(this.apAutoCmp, flux.apAutoCmp) &&
        Objects.equals(this.aperturesCmp, flux.aperturesCmp) &&
        Objects.equals(this.photo, flux.photo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rightAsc, declination, magnitude, expMiddle, deviation, addedBy, apAuto, apertures, apAutoCmp, aperturesCmp, photo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Flux {\n");
    
    sb.append("    rightAsc: ").append(toIndentedString(rightAsc)).append("\n");
    sb.append("    declination: ").append(toIndentedString(declination)).append("\n");
    sb.append("    magnitude: ").append(toIndentedString(magnitude)).append("\n");
    sb.append("    expMiddle: ").append(toIndentedString(expMiddle)).append("\n");
    sb.append("    deviation: ").append(toIndentedString(deviation)).append("\n");
    sb.append("    addedBy: ").append(toIndentedString(addedBy)).append("\n");
    sb.append("    apAuto: ").append(toIndentedString(apAuto)).append("\n");
    sb.append("    apertures: ").append(toIndentedString(apertures)).append("\n");
    sb.append("    apAutoCmp: ").append(toIndentedString(apAutoCmp)).append("\n");
    sb.append("    aperturesCmp: ").append(toIndentedString(aperturesCmp)).append("\n");
    sb.append("    photo: ").append(toIndentedString(photo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
