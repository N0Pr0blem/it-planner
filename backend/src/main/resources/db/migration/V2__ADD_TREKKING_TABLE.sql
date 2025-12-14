create table task_trekking
(
    id              BIGSERIAL PRIMARY KEY,
    date            DATE,
    hours           FLOAT(53),
    employee_id     BIGINT NOT NULL REFERENCES employee (id) ON DELETE CASCADE,
    task_details_id BIGINT NOT NULL REFERENCES task_details (id) ON DELETE CASCADE
);