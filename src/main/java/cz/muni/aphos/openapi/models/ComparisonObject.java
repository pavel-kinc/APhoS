package cz.muni.aphos.openapi.models;

import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name="ComparisonObject")
public record ComparisonObject(ObjectFluxCount original, ObjectFluxCount reference, List<FluxUserTime> data){}
