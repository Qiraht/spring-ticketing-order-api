--liquibase formatted sql
--changeset Qiraht:1
--comment: add user_id column to events table

ALTER TABLE events
ADD COLUMN user_id VARCHAR(36) NOT NULL;

ALTER TABLE events
ADD CONSTRAINT fk_events_user_id
FOREIGN KEY (user_id) REFERENCES users(id);