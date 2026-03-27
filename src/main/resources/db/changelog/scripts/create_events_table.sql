--liquibase formatted sql
--changeset id:1 author:Qiraht
--comment: first event table version

CREATE  TABLE IF NOT EXISTS events (
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(50) NOT NULL,
    description TEXT,
    location VARCHAR(100) NOT NULL,
    event_date DATETIME NOT NULL,
    price NUMERIC(5,2) NOT NULL,
    capacity INT NOT NULL,
    available INT NOT NULL,
    created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) NOT NULL,
    deleted_at TIMESTAMP(3) NULL
)