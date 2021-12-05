package com.example.astroapp.dao;

import com.example.astroapp.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, String> {

    @Override
    public List<User> findAll();
}
