-- Drop existing tables if they exist
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
    start_time DATETIME NOT NULL,  -- Consider TIMESTAMP if time zones are required
    end_time DATETIME NOT NULL,    -- Consider TIMESTAMP if time zones are required
    duration BIGINT NOT NULL,
    description VARCHAR(255),
    day_id BIGINT,  -- Foreign key for linking to day
    CHECK (end_time > start_time),
    FOREIGN KEY (day_id) REFERENCES days(id) ON DELETE RESTRICT  -- Prevent day deletion
);

-- Insert default days
INSERT INTO days (date, day_of_week)
VALUES
    ('2025-05-12', 'MONDAY'),
    ('2025-05-13', 'TUESDAY'),
    ('2025-05-14', 'WEDNESDAY'),
    ('2025-05-15', 'THURSDAY'),
    ('2025-05-16', 'FRIDAY');

-- Insert events linked to the days
INSERT INTO events (name, start_time, end_time, duration, description, day_id)
VALUES
    ('Morning Workout', '2025-05-12T06:00:00+00:00', '2025-05-12T07:00:00+00:00', 60, 'Morning workout session', 1),
    ('Team Meeting', '2025-05-12T09:00:00+00:00', '2025-05-12T10:00:00+00:00', 60, 'Weekly team sync-up', 1),
    ('Lunch Break', '2025-05-12T12:00:00+00:00', '2025-05-12T13:00:00+00:00', 60, 'Lunch break', 1),
    ('Project Work', '2025-05-13T10:00:00+00:00', '2025-05-13T12:00:00+00:00', 120, 'Work on the new feature', 2),
    ('Client Call', '2025-05-13T14:00:00+00:00', '2025-05-13T15:00:00+00:00', 60, 'Call with the client', 2),
    ('Gym Session', '2025-05-14T17:00:00+00:00', '2025-05-14T18:00:00+00:00', 60, 'Evening gym session', 3),
    ('End of Day Review', '2025-05-15T17:00:00+00:00', '2025-05-15T18:00:00+00:00', 60, 'Review work completed', 4),
    ('Research Time', '2025-05-16T09:00:00+00:00', '2025-05-16T11:00:00+00:00', 120, 'Research for the upcoming project', 5);

-- Check the inserted data
SELECT * FROM days;
SELECT * FROM events;