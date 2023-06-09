CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    "name" VARCHAR(128) NOT NULL,
    description TEXT NOT NULL,
    image_url TEXT NOT NULL,
    price REAL NOT NULL,
    is_active BOOLEAN NOT NULL,
    stock INT NOT NULL
);
