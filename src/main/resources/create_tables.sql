-- Table for 'roles'
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Table for 'users'
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(72) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    timezone VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP NOT NULL
);

-- Table for 'user_roles' (Many-to-many relationship between users and roles)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Table for 'days'
CREATE TABLE days (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    day_of_week VARCHAR(10) NOT NULL
);

-- Table for 'events'
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    duration BIGINT NOT NULL,
    description VARCHAR(255),
    day_id BIGINT NOT NULL,
    FOREIGN KEY (day_id) REFERENCES days(id)
);