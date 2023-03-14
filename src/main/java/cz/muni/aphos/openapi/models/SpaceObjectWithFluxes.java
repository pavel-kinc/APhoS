package cz.muni.aphos.openapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.aphos.dto.Flux;
import cz.muni.aphos.dto.ObjectFluxCount;

import java.util.List;

import org.springframework.validation.annotation.Validated;


/**
 * SpaceObject with fluxes extends ObjectFluxCount and contains also fluxes of the given space object.
 */
//@Schema(allOf = ObjectFluxCount.class)
@Validated
public class SpaceObjectWithFluxes extends ObjectFluxCount {

  /**
   * Fluxes of the given space object
   */
  @JsonProperty(required = true)
  private List<Flux> fluxes = null;

  public List<Flux> getFluxes() {
    return fluxes;
  }

  public void setFluxes(List<Flux> fluxes) {
    this.fluxes = fluxes;
  }
}
