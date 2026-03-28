--liquibase formatted sql
--changeset Qiraht:1
--comment: tickets table creation

CREATE TABLE IF NOT EXISTS tickets (
    id VARCHAR(36) NOT NULL PRIMARY KEY DEFAULT (UUID()),
    event_id VARCHAR(36) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    quantity INT NOT NULL,
    status ENUM('BOOKED', 'CANCELLED') NOT NULL,
    created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);