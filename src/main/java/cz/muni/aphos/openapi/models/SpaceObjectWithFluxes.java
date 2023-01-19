package cz.muni.aphos.openapi.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonView;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import cz.muni.aphos.helper.ViewField;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;


/**
 * SpaceObjectWithFluxes
 */
//@Schema(allOf = ObjectFluxCount.class)
@Validated
public class SpaceObjectWithFluxes extends ObjectFluxCount {

  private List<Flux> fluxes = null;


  /**
   * Get fluxes
   * @return fluxes
   **/
    public List<Flux> getFluxes() {
    return fluxes;
  }

  public void setFluxes(List<Flux> fluxes) {
    this.fluxes = fluxes;
  }
}
