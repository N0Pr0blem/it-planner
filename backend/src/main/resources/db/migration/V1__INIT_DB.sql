CREATE TABLE oauth_user
(
    id                BIGSERIAL PRIMARY KEY,
    username          VARCHAR(255) UNIQUE NOT NULL,
    password          VARCHAR(255)        NOT NULL,
    enabled           BOOLEAN DEFAULT true,
    verification_code VARCHAR(255),
    oauth_role        VARCHAR(32)
);

CREATE TABLE user_info
(
    id                BIGINT PRIMARY KEY REFERENCES oauth_user (id) ON DELETE CASCADE,
    first_name        VARCHAR(100),
    second_name       VARCHAR(100),
    last_name         VARCHAR(100),
    email             VARCHAR(255) UNIQUE,
    registration_date TIMESTAMP,
    profile_image     VARCHAR(255)
);

CREATE TABLE storage
(
    id   BIGSERIAL PRIMARY KEY,
    path VARCHAR(511) NOT NULL
);

CREATE TABLE storage_file
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    mime_type     VARCHAR(255) NOT NULL,
    storage_id    BIGINT       NOT NULL REFERENCES storage (id) ON DELETE CASCADE
);

CREATE TABLE project
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    created_user  BIGINT       NOT NULL REFERENCES oauth_user (id) ON DELETE CASCADE,
    storage_id    BIGINT       NOT NULL REFERENCES storage (id) ON DELETE CASCADE
);

CREATE TABLE employee
(
    id           BIGSERIAL PRIMARY KEY,
    project_id   BIGINT NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    project_role VARCHAR(32),
    user_id      BIGINT NOT NULL REFERENCES oauth_user (id) ON DELETE CASCADE,
    UNIQUE (project_id, user_id)
);

CREATE TABLE task
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    is_completed     BOOLEAN DEFAULT false,
    complexity       VARCHAR(32),
    status           VARCHAR(32),
    urgency          VARCHAR(32),
    creation_date    TIMESTAMP,
    project_id       BIGINT       NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    from_user_id     BIGINT       NOT NULL REFERENCES employee (id) ON DELETE CASCADE,
    to_user_id       BIGINT       REFERENCES employee (id) ON DELETE SET NULL,
    storage_id       BIGINT       NOT NULL REFERENCES storage (id) ON DELETE CASCADE,
    description_file VARCHAR(255)
);


CREATE INDEX idx_oauth_user_username ON oauth_user (username);
CREATE INDEX idx_oauth_user_email ON user_info (email);
CREATE INDEX idx_project_created_user ON project (created_user);
CREATE INDEX idx_employee_project_id ON employee (project_id);
CREATE INDEX idx_employee_user_id ON employee (user_id);
CREATE INDEX idx_task_project_id ON task (project_id);

