package com.example.astroapp.dao;

import com.example.astroapp.dto.FluxUserTime;

import java.util.List;

public interface FluxDao {
    long saveFlux(String recString, String decString, Float rec,
                  Float dec, Float apertureAuto, Float apertureAutoDev, Long spaceObjectId,
                  String userId, Long photoId, Float[] apertures, Float[] apertureDevs);

    List<FluxUserTime> getFluxesByObjId(Long originalObjectId, Long referenceObjectId);

    long fluxExists(Long id);

    Long getNumberOfFluxesEstimate();
}
