package com.example.astroapp.services;

import com.example.astroapp.dao.ObjectRepo;
import com.example.astroapp.entities.SpaceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;



@Service
public class ObjectService {

    @Autowired
    ObjectRepo objectRepo;

    SpaceObject checkIfExistsAndSave(SpaceObject spaceObject) {
        try {
        SpaceObject databaseObject = objectRepo.findByCatalogID(spaceObject.getCatalogID());
        // TODO ask if the undetected objects are needed if not delete
            if (spaceObject.getCatalogID().equals("") || databaseObject != null) {
                return databaseObject;
            }
            return objectRepo.save(spaceObject);
        } catch (DataIntegrityViolationException e) {
            return objectRepo.findByCatalogID(spaceObject.getCatalogID());
        }
    }
}
