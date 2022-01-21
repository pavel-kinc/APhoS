CREATE EXTENSION IF NOT EXISTS cube;
CREATE EXTENSION IF NOT EXISTS earthdistance;

CREATE OR REPLACE FUNCTION earth() RETURNS float8
AS
'select 180 / pi();'
    LANGUAGE SQL;

CREATE TABLE IF NOT EXISTS users
(
    google_sub  VARCHAR(100) NOT NULL PRIMARY KEY,
    username    VARCHAR(50),
    description VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS object
(
    id          SERIAL NOT NULL,
    name        VARCHAR(40),
    catalog     VARCHAR(20),
    catalog_id  VARCHAR(30) UNIQUE,
    catalog_rec float4,
    catalog_dec float4,
    coordinates earth,
    catalog_mag float4,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS photo_properties
(
    id             SERIAL NOT NULL,
    exposure_begin timestamp   NOT NULL,
    exposure_end   timestamp   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS flux
(
    id                  SERIAL       NOT NULL,
    rec                 float8       NOT NULL,
    dec                 float8       NOT NULL,
    coordinates         earth,
    ap_auto             float8,
    object_id           int4         NOT NULL REFERENCES object (id),
    user_id             VARCHAR(100) NOT NULL REFERENCES users (google_sub),
    photo_properties_id int4         NOT NULL REFERENCES photo_properties (id),
    apertures           float8[],
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS aperture
(
    id      SERIAL NOT NULL,
    value   float8,
    flux_id int4   NOT NULL REFERENCES flux (id),
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS flux_rec
    ON flux (rec);

CREATE INDEX IF NOT EXISTS flux_dec
    ON flux (dec);

CREATE INDEX IF NOT EXISTS object_catalog_id
    ON object (catalog_id);
