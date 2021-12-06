package com.example.astroapp.dao;

import com.example.astroapp.entities.SpaceObject;
import org.springframework.data.repository.CrudRepository;

public interface ObjectRepo extends CrudRepository<SpaceObject, Long> {
}
