package cz.muni.aphos.openapi.models;

import cz.muni.aphos.dto.FluxUserTime;
import cz.muni.aphos.dto.ObjectFluxCount;

import java.util.List;

public record ComparisonObject(ObjectFluxCount original, ObjectFluxCount reference, List<FluxUserTime> data){}
