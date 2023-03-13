package cz.muni.aphos.openapi.models;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

/**
 * Coordinates
 */
@Schema(defaultValue = "", example = "{\"rightAsc\":\"21:41:55.291\",\"declination\":\"71:18:41.12\",\"radius\":0.05}")
@Validated
public class Coordinates   {

  private static final ValidatorFactory fac = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = fac.getValidator();

  @Pattern(regexp="\\d{2}:\\d{2}:\\d{2}([.]\\d+)?|^$")
  @NotBlank
  private String rightAsc = "";

  @Pattern(regexp="[+-]?\\d{2}:\\d{2}:\\d{2}([.]\\d+)?|^$")
  @NotBlank
  private String declination = "";

  @Min(0)
  @NotNull
  private Double radius = 0.0;

  public Coordinates() {

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

  public boolean isValid(){
    Set<ConstraintViolation<Coordinates>> violations = validator.validate(this);
    return violations.isEmpty();
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
