-- Check if the tables exist and drop them if they do
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS days;

-- Create the `days` table first
CREATE TABLE days (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    day_of_week VARCHAR(10) NOT NULL
);

-- Create the `events` table with a foreign key reference to `days`
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    duration BIGINT NOT NULL,
    description VARCHAR(255),
    day_id BIGINT,
    CHECK (end_time > start_time),
    FOREIGN KEY (day_id) REFERENCES days(id) ON DELETE CASCADE
);