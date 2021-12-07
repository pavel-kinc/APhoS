package com.example.astroapp.services;

import com.example.astroapp.dao.ApertureRepo;
import com.example.astroapp.dao.FluxRepo;
import com.example.astroapp.dao.ObjectRepo;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.entities.PhotoProperties;
import com.example.astroapp.entities.SpaceObject;
import com.example.astroapp.utils.UnitConversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Map;

@Service
public class RowDataParserService {

    @Autowired
    ApertureRepo apertureRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    FluxRepo fluxRepo;
    @Autowired
    ObjectRepo objectRepo;

    private PhotoProperties photoProperties;

    public void saveRow(Map<String, String> row) throws ParseException {
        SpaceObject spaceObject = saveObject(row.get("Name"), row.get("Catalog"),
                row.get("CatalogId"), row.get("CatalogRA"), row.get("CatalogDec"), row.get("CatalogMag"));
    }

    public SpaceObject saveObject(String name, String catalog, String catalogID,
                                  String catalogRec, String catalogDec, String catalogMag) throws ParseException {
        SpaceObject spaceObject;
        SpaceObject databaseObject = objectRepo.findByCatalogID(catalogID);
        if (databaseObject != null) {
            spaceObject = databaseObject;
        } else {
            spaceObject = new SpaceObject();
            spaceObject.setName(name);
            if (!catalogID.equals("")) {
                spaceObject.setCatalog(catalog);
                spaceObject.setCatalogID(catalogID);
                float dec = UnitConversions.angleToFloatForm(catalogDec);
                spaceObject.setCatalogDec(dec);
                float rec = UnitConversions.hourAngleToDegrees(catalogRec);
                spaceObject.setCatalogRec(rec);
                spaceObject.setCatalogMag(Float.parseFloat(catalogMag));
            }
            objectRepo.save(spaceObject);
        }
        return spaceObject;
    }

    public PhotoProperties getPhotoProperties() {
        return photoProperties;
    }

    public void setPhotoProperties(PhotoProperties photoProperties) {
        this.photoProperties = photoProperties;
    }
}
