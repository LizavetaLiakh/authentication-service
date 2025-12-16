--liquibase formatted sql

--changeset LizavetaLiakh:auth1_tables
CREATE TABLE authentication_users(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(5) NOT NULL
);