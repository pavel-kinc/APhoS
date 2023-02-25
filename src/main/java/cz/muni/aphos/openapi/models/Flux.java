package cz.muni.aphos.openapi.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import cz.muni.aphos.dto.PhotoProperties;
import cz.muni.aphos.helper.ViewField;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * Flux
 */
@Validated
//@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-23T18:56:03.993Z[GMT]")
public class Flux   {
  @JsonProperty("rightAsc")
  @JsonView(ViewField.Public.class)
  private String rightAsc = null;

  @JsonProperty("declination")
  @JsonView(ViewField.Public.class)
  private String declination = null;

  @JsonProperty("addedBy")
  @JsonView(ViewField.Public.class)
  private String addedBy = null;

  @JsonProperty("apAuto")
  @JsonView(ViewField.Public.class)
  private Double apAuto = null;

  @JsonProperty("apertures")
  @JsonView(ViewField.Public.class)
  @Valid
  private List<Double> apertures = null;

  @JsonProperty("photo")
  @JsonView(ViewField.Public.class)
  private PhotoProperties photo = null;

  @JsonProperty("apAutoDev")
  @JsonView(ViewField.Public.class)
  private Double apAutoDev = null;

  @JsonProperty("apertureDevs")
  @JsonView(ViewField.Public.class)
  private List<Double> apertureDevs = null;

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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Flux flux = (Flux) o;
    return Objects.equals(this.rightAsc, flux.rightAsc) &&
        Objects.equals(this.declination, flux.declination) &&
        Objects.equals(this.addedBy, flux.addedBy) &&
        Objects.equals(this.apAuto, flux.apAuto) &&
        Objects.equals(this.apertures, flux.apertures) &&
        Objects.equals(this.photo, flux.photo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rightAsc, declination, addedBy, apAuto, apertures, photo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Flux {\n");
    
    sb.append("    rightAsc: ").append(toIndentedString(rightAsc)).append("\n");
    sb.append("    declination: ").append(toIndentedString(declination)).append("\n");
    sb.append("    addedBy: ").append(toIndentedString(addedBy)).append("\n");
    sb.append("    apAuto: ").append(toIndentedString(apAuto)).append("\n");
    sb.append("    apertures: ").append(toIndentedString(apertures)).append("\n");
    sb.append("    photo: ").append(toIndentedString(photo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public Double getApAutoDev() {
    return apAutoDev;
  }

  public void setApAutoDev(Double apAutoDev) {
    this.apAutoDev = apAutoDev;
  }

  public List<Double> getApertureDevs() {
    return apertureDevs;
  }

  public void setApertureDevs(List<Double> apertureDevs) {
    this.apertureDevs = apertureDevs;
  }
}
