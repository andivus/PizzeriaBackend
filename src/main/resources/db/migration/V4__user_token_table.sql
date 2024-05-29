CREATE TABLE IF NOT EXISTS user_tokens (
    id SERIAL PRIMARY KEY,
    "user" uuid NOT NULL,
    token TEXT NOT NULL,
    valid_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_tokens_user__id FOREIGN KEY ("user") REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT
    );
