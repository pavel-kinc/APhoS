package cz.muni.aphos.openapi.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Catalog
 */
public enum Catalog {
  UCAC4("UCAC4"),
  USNO_B1_0("USNO-B1.0");

  private String value;

  Catalog(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
