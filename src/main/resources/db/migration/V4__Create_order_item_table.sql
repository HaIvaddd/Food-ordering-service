-- NOSONAR: VARCHAR is correct for PostgreSQL
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    count INT NOT NULL CHECK (count > 0),
    total_price DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_food FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE
);
