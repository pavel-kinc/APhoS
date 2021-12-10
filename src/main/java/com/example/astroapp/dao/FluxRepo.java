package com.example.astroapp.dao;

import com.example.astroapp.entities.Flux;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FluxRepo extends CrudRepository<Flux, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value=
            "UPDATE flux SET coordinates=ll_to_earth(dec, rec)" +
                    "WHERE coordinates IS NULL")
    void updateCoordinates();
}
