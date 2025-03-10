-- NOSONAR: VARCHAR is correct for PostgreSQL
CREATE TABLE food (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    food_type VARCHAR(50) NOT NULL
);