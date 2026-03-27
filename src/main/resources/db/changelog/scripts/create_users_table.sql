--liquibase formatted sql
--changeset Qiraht:1
--comment: users table creation

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    email VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(16) NOT NULL,
    last_name VARCHAR(16) NOT NULL,
    password TEXT NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    deleted_at TIMESTAMP(3) NULL
);