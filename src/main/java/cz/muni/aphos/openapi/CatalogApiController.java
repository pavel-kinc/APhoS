package cz.muni.aphos.openapi;

import cz.muni.aphos.openapi.models.Catalog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogApiController implements CatalogApi {
    @Override
    public ResponseEntity<String[]> getCatalogs() {
        return new ResponseEntity<>(Catalog.getCatalogs(), HttpStatus.OK);
    }
}
