create table task_tracking
(
    id          BIGSERIAL PRIMARY KEY,
    date        DATE,
    hours       FLOAT(53),
    employee_id BIGINT NOT NULL REFERENCES employee (id) ON DELETE CASCADE,
    task_id     BIGINT NOT NULL REFERENCES task (id) ON DELETE CASCADE
);