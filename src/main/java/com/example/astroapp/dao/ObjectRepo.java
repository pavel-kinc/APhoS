package com.example.astroapp.dao;

import com.example.astroapp.entities.SpaceObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ObjectRepo extends CrudRepository<SpaceObject, Long> {

    @Override
    List<SpaceObject> findAll();

    @Query(nativeQuery = true, value=
            "SELECT * FROM object where catalog_id = ?1")
    SpaceObject findByCatalogID(String catalogID);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value=
            "UPDATE object SET coordinates=ll_to_earth(catalog_dec,catalog_rec)" +
                    "WHERE coordinates IS NULL")
    void updateCoordinates();


}
