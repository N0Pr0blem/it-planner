CREATE TABLE ai_request
(
    id            BIGSERIAL PRIMARY KEY,
    pattern       VARCHAR(128) NOT NULL,
    request          TEXT        NOT NULL,
    send_date TIMESTAMP,
    author_id     BIGINT      NOT NULL REFERENCES user_info (id) ON DELETE CASCADE
);
