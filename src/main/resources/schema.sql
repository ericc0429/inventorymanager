DROP TABLE items;

CREATE TABLE IF NOT EXISTS artists (
id    VARCHAR(60)   DEFAULT RANDOM_UUID() PRIMARY KEY,
name  VARCHAR       NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
id        VARCHAR(60)   DEFAULT RANDOM_UUID() PRIMARY KEY,
gid       VARCHAR(60),
category  VARCHAR       NOT NULL,
name      VARCHAR       NOT NULL,
version   VARCHAR,
released  VARCHAR       NOT NULL,
price     VARCHAR       NOT NULL
);