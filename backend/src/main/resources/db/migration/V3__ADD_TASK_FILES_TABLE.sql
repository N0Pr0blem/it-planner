create table task_file
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(64),
    type            VARCHAR(32),
    task_id     BIGINT NOT NULL REFERENCES task_info (id) ON DELETE CASCADE
);