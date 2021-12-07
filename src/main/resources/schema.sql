CREATE EXTENSION IF NOT EXISTS cube;
CREATE EXTENSION IF NOT EXISTS earthdistance;

CREATE OR REPLACE FUNCTION earth() RETURNS float8
AS
'select 180 / pi();'
    LANGUAGE SQL;

CREATE TABLE IF NOT EXISTS users
(
    google_sub VARCHAR(100) NOT NULL PRIMARY KEY,
    email      VARCHAR(100) NULL,
    first_name VARCHAR(30)  NULL,
    last_name  VARCHAR(30)  NULL
);

CREATE TABLE IF NOT EXISTS object
(
    id          SERIAL NOT NULL,
    name        VARCHAR(40),
    catalog     VARCHAR(20),
    catalog_id  VARCHAR(30),
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
    rec                 earth        NOT NULL,
    dec                 earth        NOT NULL,
    ap_auto             float8,
    object_id           int4         NOT NULL REFERENCES object (id),
    user_id             VARCHAR(100) NOT NULL REFERENCES users (google_sub),
    photo_properties_id int4         NOT NULL REFERENCES photo_properties (id),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS aperture
(
    id     SERIAL NOT NULL,
    value  float8,
    flux_id int4   NOT NULL REFERENCES flux (id),
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS flux_rec
    ON flux (rec);

CREATE INDEX IF NOT EXISTS flux_dec
    ON flux (dec);
