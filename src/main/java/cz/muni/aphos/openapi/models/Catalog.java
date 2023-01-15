package cz.muni.aphos.openapi.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Gets or Sets Catalog
 */
@Schema(type="String", allowableValues = {"UCAC4", "USNO-B1.0"})
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
}
