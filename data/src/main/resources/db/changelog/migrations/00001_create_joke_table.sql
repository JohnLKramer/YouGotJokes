--liquibase formatted sql

--changeset jkramer:1
CREATE TABLE joke (
    id         BIGSERIAL PRIMARY KEY,
    joke      TEXT NOT NULL,
    description TEXT NULL,
    date  DATE NOT NULL
);
