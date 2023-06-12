CREATE TABLE IF NOT EXISTS orders
(
    id             UUID PRIMARY KEY,
    purchase_date  TIMESTAMP NOT NULL,
    first_name     VARCHAR(256) NOT NULL,
    last_name      VARCHAR(256) NOT NULL,
    email          VARCHAR(256) NOT NULL,
    phone          INT NOT NULL,
    city           VARCHAR(256) NOT NULL,
    zip_code       INT NOT NULL,
    first_address  TEXT NOT NULL,
    second_address TEXT NULL,
    total_price    REAL NOT NULL,
    status         VARCHAR(24) NOT NULL,
    user_ip        VARCHAR(15) NULL,
    user_agent     TEXT NULL
    );

CREATE TABLE IF NOT EXISTS order_items
(
    id          SERIAL PRIMARY KEY,
    "order"     UUID NOT NULL,
    item        INT NOT NULL,
    item_amount INT NOT NULL,
    CONSTRAINT fk_order_items_order__id FOREIGN KEY ("order") REFERENCES orders
(id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_order_items_item__id FOREIGN KEY (item) REFERENCES items(id)
     ON DELETE RESTRICT ON UPDATE RESTRICT
    );

ALTER TABLE order_items ADD CONSTRAINT order_items_order_item_unique UNIQUE ("order", item);
