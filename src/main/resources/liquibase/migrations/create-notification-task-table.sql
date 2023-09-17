-- liquibase formatted sql

-- changeset tsalikhov:1
CREATE TABLE IF NOT EXISTS notification_task
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    notification_date_time TIMESTAMP NOT NULL
);