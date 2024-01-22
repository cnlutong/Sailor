CREATE
DATABASE IF NOT EXISTS sailor;

CREATE TABLE IF NOT EXISTS sailor.Users
(
    id       int auto_increment,
    uuid     VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    encode   VARCHAR(255) NOT NULL,
    time     DATETIME     NOT NULL,
    PRIMARY KEY (id)
    );


CREATE TABLE IF NOT EXISTS sailor.ServerInterfaces
(
    id                  INT AUTO_INCREMENT,
    uuid                VARCHAR(255) NOT NULL,
    server_Interface_Name VARCHAR(255) NOT NULL,
    interface_Key_UUID    VARCHAR(255) NOT NULL,
    address             VARCHAR(255) NOT NULL,
    listen_Port          VARCHAR(255) NOT NULL,
    eth_Port             VARCHAR(255) NOT NULL,
    time                DATETIME     NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS sailor.ClientInterfaces
(
    id                  INT AUTO_INCREMENT,
    uuid                VARCHAR(255) NOT NULL,
    client_Name          VARCHAR(255) NOT NULL,
    interface_Key_UUID    VARCHAR(255) NOT NULL,
    Server_Interface_UUID VARCHAR(255) NOT NULL,
    address             VARCHAR(255) NOT NULL,
    dns                 VARCHAR(255) NOT NULL,
    persistent_Keepalive VARCHAR(255) NOT NULL,
    time                DATETIME     NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS sailor.InterfaceKeys
(
    id         INT AUTO_INCREMENT,
    uuid       VARCHAR(255) NOT NULL,
    public_Key  VARCHAR(255) NOT NULL,
    private_Key VARCHAR(255) NOT NULL,
    time       DATETIME     NOT NULL,
    PRIMARY KEY (id)
    );
