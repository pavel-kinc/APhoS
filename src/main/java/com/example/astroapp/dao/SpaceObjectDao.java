package com.example.astroapp.dao;

import com.example.astroapp.dto.ObjectFluxCount;
import com.example.astroapp.dto.SpaceObject;

import java.util.List;

public interface SpaceObjectDao {
    List<ObjectFluxCount> queryObjects(String RA, String dec, String radius,
                                       String name, String minMag,
                                       String maxMag, String catalog, String objectId);

    long saveObject(String catalogId, String name, String catalog, String strDec, String strRec,
                    Float catalogDec, Float catalogRec, Float catalogMag);

    List<String> getAvailableCatalogues();

    SpaceObject getSpaceObjectById(Long id);

    Long getNumberOfObjectsEstimate();
}
