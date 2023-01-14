package cz.muni.aphos.openapi.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;


/**
 * SpaceObjectWithFluxes
 */
@Validated
//@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-12-23T18:56:03.993Z[GMT]")
public class SpaceObjectWithFluxes extends ObjectFluxCount {
  @Valid
  private List<Flux> fluxes = null;


  /**
   * Get fluxes
   * @return fluxes
   **/
  @Schema(description = "")
      @Valid
    public List<Flux> getFluxes() {
    return fluxes;
  }

  public void setFluxes(List<Flux> fluxes) {
    this.fluxes = fluxes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpaceObjectWithFluxes spaceObjectWithFluxes = (SpaceObjectWithFluxes) o;
    return Objects.equals(this.fluxes, spaceObjectWithFluxes.fluxes) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fluxes, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpaceObjectWithFluxes {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    fluxes: ").append(toIndentedString(fluxes)).append("\n");
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
