DROP TABLE albums;
/* DROP TABLE artists;
DROP TABLE groups; */

CREATE TABLE IF NOT EXISTS groups (
id        VARCHAR(60)   DEFAULT RANDOM_UUID() PRIMARY KEY,
name      VARCHAR       NOT NULL,
type      VARCHAR       NOT NULL,
members   VARCHAR       NOT NULL
);

CREATE TABLE IF NOT EXISTS artists (
id        VARCHAR(60)   DEFAULT RANDOM_UUID() PRIMARY KEY,
groups    VARCHAR,
name      VARCHAR       NOT NULL,
debut     VARCHAR       NOT NULL,
label     VARCHAR,
gender    VARCHAR       NOT NULL,
birthday  VARCHAR       NOT NULL,
assets    VARCHAR
);

CREATE TABLE IF NOT EXISTS albums (
id        VARCHAR(60)   DEFAULT RANDOM_UUID() PRIMARY KEY,
artist    VARCHAR(60),  NOT NULL,
type      VARCHAR       NOT NULL,
name      VARCHAR       NOT NULL,
version   VARCHAR,
variation VARCHAR,
color     VARCHAR,
extras    VARCHAR,
released  VARCHAR       NOT NULL,
price     VARCHAR       NOT NULL,
barcode   VARCHAR
);