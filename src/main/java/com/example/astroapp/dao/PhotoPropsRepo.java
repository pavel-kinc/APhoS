package com.example.astroapp.dao;

import com.example.astroapp.entities.PhotoProperties;
import org.springframework.data.repository.CrudRepository;

public interface PhotoPropsRepo extends CrudRepository<PhotoProperties, Long> {
}
