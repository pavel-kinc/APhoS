package cz.muni.aphos.openapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name="ComparisonObject")
public record ComparisonObject(@JsonProperty(required = true) ObjectFluxCount variable,
                               @JsonProperty(required = true) ObjectFluxCount comparative,
                               @JsonProperty(required = true) List<FluxUserTime> data){}
