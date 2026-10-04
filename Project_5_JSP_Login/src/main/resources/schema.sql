-- Users table. Runs on H2 (MySQL mode) and on MySQL as-is.
-- Executed automatically at startup by AppContextListener.
CREATE TABLE IF NOT EXISTS users (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    user_id       VARCHAR(20)  NOT NULL UNIQUE,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    email         VARCHAR(254) NOT NULL UNIQUE,
    date_of_birth DATE         NOT NULL,
    photo         MEDIUMBLOB   NOT NULL,
    photo_type    VARCHAR(20)  NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
