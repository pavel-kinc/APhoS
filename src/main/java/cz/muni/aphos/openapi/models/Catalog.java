package cz.muni.aphos.openapi.models;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;

/**
 * Gets or Sets Catalog
 */
@Schema(name="Catalog", type="String",allowableValues = {"UCAC4", "USNO-B1.0"}, enumAsRef = true)
@Validated
public class Catalog {

  public static final String defaultValue = "UCAC4";
  private String value;

  public Catalog(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public static String[] getCatalogs(){
    return Catalog.class.getAnnotation(Schema.class).allowableValues();
  }
}
