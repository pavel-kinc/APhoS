package cz.muni.aphos.dao;

import cz.muni.aphos.dto.PhotoProperties;

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
