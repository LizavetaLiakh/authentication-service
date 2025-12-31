--liquibase formatted sql

--changeset LizavetaLiakh:auth1_tables
CREATE TABLE authentication_users(
    id BIGINT PRIMARY KEY NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(5) NOT NULL
);