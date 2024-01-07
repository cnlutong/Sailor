CREATE DATABASE IF NOT EXISTS sailor;

CREATE TABLE IF NOT EXISTS sailor.Users
(
    id       int auto_increment,
    uuid     VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    encode   VARCHAR(255) NOT NULL,
    time     datetime     NOT NULL,
    PRIMARY KEY (id)
);
