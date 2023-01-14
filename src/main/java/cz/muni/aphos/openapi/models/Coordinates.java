package cz.muni.aphos.openapi.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

/**
 * Coordinates
 */
@Validated
public class Coordinates   {
  @NotBlank
  private String rightAsc = "";

  @NotBlank
  private String declination = "";

  @Min(0)
  private Double radius = null;

  public Coordinates rightAsc(String rightAsc) {
    this.rightAsc = rightAsc;
    return this;
  }

  /**
   * Get rightAsc
   * @return rightAsc
   **/
  @Schema(description = "Right ascension")
  
    public String getRightAsc() {
    return rightAsc;
  }

  public void setRightAsc(String rightAsc) {
    this.rightAsc = rightAsc;
  }

  public Coordinates declination(String declination) {
    this.declination = declination;
    return this;
  }

  /**
   * Get declination
   * @return declination
   **/
  @Schema(description = "Declination")
  
    public String getDeclination() {
    return declination;
  }

  public void setDeclination(String declination) {
    this.declination = declination;
  }

  public Coordinates radius(Double radius) {
    this.radius = radius;
    return this;
  }

  /**
   * Get radius
   * @return radius
   **/
  @Schema(description = "Radius")
  
    public Double getRadius() {
    return radius;
  }

  public void setRadius(Double radius) {
    this.radius = radius;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coordinates coordinates = (Coordinates) o;
    return Objects.equals(this.rightAsc, coordinates.rightAsc) &&
        Objects.equals(this.declination, coordinates.declination) &&
        Objects.equals(this.radius, coordinates.radius);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rightAsc, declination, radius);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Coordinates {\n");
    
    sb.append("    rightAsc: ").append(toIndentedString(rightAsc)).append("\n");
    sb.append("    declination: ").append(toIndentedString(declination)).append("\n");
    sb.append("    radius: ").append(toIndentedString(radius)).append("\n");
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
