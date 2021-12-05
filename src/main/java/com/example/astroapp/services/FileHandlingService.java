package com.example.astroapp.services;

import com.example.astroapp.dao.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileHandlingService {

    @Autowired
    UserRepo userRepo;

    void store(MultipartFile file) {

    }

}
