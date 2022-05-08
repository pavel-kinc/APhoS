package cz.muni.var4astro.dao;

import cz.muni.var4astro.dto.PhotoProperties;

/**
 * The interface Photo properties dao, data access object for the photo_properties entity.
 */
public interface PhotoPropertiesDao {

    /**
     * Save photo props to the database.
     *
     * @param photoProperties the photo properties object
     */
    void savePhotoProps(PhotoProperties photoProperties);
}
