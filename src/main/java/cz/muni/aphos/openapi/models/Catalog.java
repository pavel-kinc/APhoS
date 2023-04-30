package cz.muni.aphos.openapi.models;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * Object used as enum(mainly for swagger-ui) with some allowable values in one field
 */
@Schema(name = "Catalog", type = "String", allowableValues = {"All catalogues", "UCAC4", "USNO-B1.0"}, enumAsRef = true)
@Validated
public class Catalog {

    /**
     * Default value for catalog
     */
    public static final String defaultValue = "UCAC4";

    /**
     * Special value for searching in all catalogs
     */
    public static final String allValue = "All catalogues";

    private String value;

    public Catalog(String value) {
        this.value = value;
    }

    public static String[] getCatalogs() {
        return Catalog.class.getAnnotation(Schema.class).allowableValues();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
