package com.example.astroapp.dao;

import com.example.astroapp.entities.Flux;
import org.springframework.data.repository.CrudRepository;

public interface FluxRepo extends CrudRepository<Flux, Long> {
}
