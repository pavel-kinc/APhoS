package cz.muni.aphos.openapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Comparison object containing variable star, comparison star and var-comp data containing astronomy data.
 * Implemented as record.
 *
 * @param variable   variable space objects
 * @param comparison comparison space objects
 * @param data       computed data for given space objects
 */
@Schema(name = "ComparisonObject")
public record ComparisonObject(@JsonProperty(required = true) ObjectFluxCount variable,
                               @JsonProperty(required = true) ObjectFluxCount comparison,
                               @JsonProperty(required = true) List<FluxUserTime> data) {
}
