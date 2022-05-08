package cz.muni.var4astro.dao;

import cz.muni.var4astro.dto.FluxUserTime;

import java.util.List;

/**
 * The interface Flux dao, data access object for the queries involving the flux entity.
 */
public interface FluxDao {
    /**
     * Save flux to the database.
     *
     * @param recString       the right ascension string
     * @param decString       the declination string
     * @param rec             the right ascension float
     * @param dec             the declination float
     * @param apertureAuto    the aperture auto
     * @param apertureAutoDev the aperture auto deviation
     * @param spaceObjectId   the space_object id
     * @param userId          the uploading user id
     * @param photoId         the photo_properties id
     * @param apertures       the apertures
     * @param apertureDevs    the aperture deviations
     * @return the generated id
     */
    long saveFlux(String recString, String decString, Float rec,
                  Float dec, Float apertureAuto, Float apertureAutoDev, Long spaceObjectId,
                  String userId, Long photoId, Float[] apertures, Float[] apertureDevs);

    /**
     * Gets fluxes, reference flux values, uploading user, and corresponding nights
     * of a space object with a given space_object id and reference space_object id.
     *
     * @param originalObjectId  the original space object database id
     * @param referenceObjectId the reference space object database id
     * @return The List of FluxUserTime objects ({@see cz/muni/var4astro/dto/FluxUserTime.java})
     */
    List<FluxUserTime> getFluxesByObjId(Long originalObjectId, Long referenceObjectId);

    /**
     * Flux exists?
     *
     * @param id the id
     * @return the id if the object exists
     */
    long fluxExists(Long id);

    /**
     * Gets a statistical estimate of the number of space_objects in the database.
     *
     * @return the number of fluxes estimate
     */
    Long getNumberOfFluxesEstimate();
}
