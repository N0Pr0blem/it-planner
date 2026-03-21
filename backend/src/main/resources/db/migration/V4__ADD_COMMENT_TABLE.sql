CREATE TABLE comment
(
    id            BIGSERIAL PRIMARY KEY,
    text          TEXT NOT NULL,
    creation_date TIMESTAMP,
    author_id     BIGINT NOT NULL REFERENCES user_info (id) ON DELETE CASCADE,
    task_id       BIGINT NOT NULL REFERENCES task (id) ON DELETE CASCADE
);
