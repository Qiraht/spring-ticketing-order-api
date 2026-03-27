--liquibase formatted sql
--changeset Qiraht:1
--comment: add status column to events table

ALTER TABLE events
ADD COLUMN status ENUM('ACTIVE', 'FINISHED') NOT NULL;