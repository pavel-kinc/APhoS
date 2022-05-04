package cz.muni.var4astro.dao;

import cz.muni.var4astro.dto.ObjectFluxCount;
import cz.muni.var4astro.dto.SpaceObject;

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
