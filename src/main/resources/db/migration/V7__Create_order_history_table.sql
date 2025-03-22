CREATE TABLE orders_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL,
    created_at TIMESTAMP NOT NULL,
    food_items JSONB NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
)