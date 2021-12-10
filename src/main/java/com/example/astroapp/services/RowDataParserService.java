package com.example.astroapp.services;

import com.example.astroapp.dao.ApertureRepo;
import com.example.astroapp.dao.FluxRepo;
import com.example.astroapp.dao.ObjectRepo;
import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.entities.*;
import com.example.astroapp.utils.UnitConversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Service
public class RowDataParserService {

    @Autowired
    ApertureRepo apertureRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    FluxRepo fluxRepo;
    @Autowired
    ObjectService objectService;


    private PhotoProperties photoProperties;
    private User uploadingUser;

    public void saveRow(Map<String, String> row) throws ParseException {
        SpaceObject spaceObject = saveObject(row.get("Name"), row.get("Catalog"),
                row.get("CatalogId"), row.get("CatalogRA"), row.get("CatalogDec"), row.get("CatalogMag"));
        Flux flux = saveFlux(row.get("RA"), row.get("Dec"),
                row.get("ApAuto"), spaceObject, photoProperties);
        int i = 1;
        String aperture;
        List<Aperture> apertures = new ArrayList<>();
        // getting all columns in form of Ap1..Apn
        while ((aperture = row.get("Ap"+i)) != null) {
            apertures.add(new Aperture(flux, aperture));
            i++;
        }
        apertureRepo.saveAll(apertures);
    }

    public SpaceObject saveObject(String name, String catalog, String catalogID,
                                  String catalogRec, String catalogDec, String catalogMag) throws ParseException {
        SpaceObject spaceObject = new SpaceObject();
        spaceObject.setName(name);
        spaceObject.setCatalog(catalog);
        spaceObject.setCatalogID(catalogID);
        float dec = UnitConversions.angleToFloatForm(catalogDec);
        spaceObject.setCatalogDec(dec);
        float rec = UnitConversions.hourAngleToDegrees(catalogRec);
        spaceObject.setCatalogRec(rec);
        spaceObject.setCatalogMag(Float.parseFloat(catalogMag));
        return objectService.checkIfExistsAndSave(spaceObject);
    }

    public Flux saveFlux(String strRec, String strDec, String ap_auto,
                         SpaceObject object, PhotoProperties photoProperties) throws ParseException {
        Flux flux = new Flux();
        float dec = UnitConversions.angleToFloatForm(strDec);
        flux.setDec(dec);
        float rec = UnitConversions.hourAngleToDegrees(strRec);
        flux.setRec(rec);
        flux.setApertureAuto(ap_auto);
        flux.setObject(object);
        flux.setPhotoProperty(photoProperties);
        flux.setUser(uploadingUser);
        fluxRepo.save(flux);
        return flux;
    }

    public void saveAperture(String apertureStr, Flux flux) {
        Aperture aperture = new Aperture();
        aperture.setValue(apertureStr);
        aperture.setFlux(flux);
        apertureRepo.save(aperture);
    }

    public PhotoProperties getPhotoProperties() {
        return photoProperties;
    }

    public void setPhotoProperties(PhotoProperties photoProperties) {
        this.photoProperties = photoProperties;
    }

    public void setUploadingUser(User uploadingUser) {
        this.uploadingUser = uploadingUser;
    }
}
