-- Check if the table exists and drop it if it does
DROP TABLE IF EXISTS events;

-- Create a new table with the following columns
CREATE TABLE events (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    duration BIGINT NOT NULL,
    description VARCHAR(255),
    CHECK (end_time > start_time)
);

