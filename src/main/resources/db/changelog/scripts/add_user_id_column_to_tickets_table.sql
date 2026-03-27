--liquibase formatted sql
--changeset Qiraht:1
--comment: add user_id column to tickets table

ALTER TABLE tickets
ADD COLUMN user_id VARCHAR(36) NOT NULL;

ALTER TABLE tickets
ADD CONSTRAINT fk_tickets_user_id
FOREIGN KEY (user_id) REFERENCES users(id);