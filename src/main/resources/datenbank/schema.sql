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
    serverInterfaceName VARCHAR(255) NOT NULL,
    interfaceKeyUUID    VARCHAR(255) NOT NULL,
    address             VARCHAR(255) NOT NULL,
    listenPort          VARCHAR(255) NOT NULL,
    ethPort             VARCHAR(255) NOT NULL,
    time                DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sailor.ClientInterfaces
(
    id                  INT AUTO_INCREMENT,
    uuid                VARCHAR(255) NOT NULL,
    clientName          VARCHAR(255) NOT NULL,
    interfaceKeyUUID    VARCHAR(255) NOT NULL,
    ServerInterfaceUUID VARCHAR(255) NOT NULL,
    address             VARCHAR(255) NOT NULL,
    dns                 VARCHAR(255) NOT NULL,
    persistentKeepalive VARCHAR(255) NOT NULL,
    time                DATETIME     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sailor.InterfaceKeys
(
    id         INT AUTO_INCREMENT,
    uuid       VARCHAR(255) NOT NULL,
    publicKey  VARCHAR(255) NOT NULL,
    privateKey VARCHAR(255) NOT NULL,
    time       DATETIME     NOT NULL,
    PRIMARY KEY (id)
);
