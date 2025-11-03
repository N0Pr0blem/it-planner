CREATE TABLE oauth_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT true,
    verification_code VARCHAR(255),
    oauth_role VARCHAR(32)
);

CREATE TABLE user_info (
    id BIGINT PRIMARY KEY REFERENCES oauth_user(id) ON DELETE CASCADE,
    first_name VARCHAR(100),
    second_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255) UNIQUE,
    registration_date TIMESTAMP,
    profile_image VARCHAR(255)
);

CREATE TABLE project (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    created_user BIGINT NOT NULL REFERENCES oauth_user(id) ON DELETE CASCADE
);

CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE,
    project_role VARCHAR(32),
    user_id BIGINT NOT NULL REFERENCES oauth_user(id) ON DELETE CASCADE,
    UNIQUE(project_id, user_id)
);

CREATE TABLE project_repository (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE REFERENCES project(id) ON DELETE CASCADE,
    path VARCHAR(64)
);

CREATE TABLE project_repository_file (
    id BIGSERIAL PRIMARY KEY,
    project_repository_id BIGINT NOT NULL REFERENCES project_repository(id) ON DELETE CASCADE,
    type VARCHAR(32) NOT NULL,
    name TEXT
);

CREATE TABLE task_info (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_completed BOOLEAN DEFAULT false,
    complexity VARCHAR(32),
    urgency VARCHAR(32),
    creation_date TIMESTAMP,
    project_id BIGINT NOT NULL REFERENCES project(id) ON DELETE CASCADE
);

CREATE TABLE task_details (
    id BIGSERIAL PRIMARY KEY,
    from_user_id BIGINT NOT NULL REFERENCES oauth_user(id) ON DELETE CASCADE,
    to_user_id BIGINT REFERENCES oauth_user(id) ON DELETE SET NULL,
    description_file VARCHAR(255)
);

CREATE INDEX idx_oauth_user_username ON oauth_user(username);
CREATE INDEX idx_oauth_user_email ON user_info(email);
CREATE INDEX idx_project_created_user ON project(created_user);
CREATE INDEX idx_employee_project_id ON employee(project_id);
CREATE INDEX idx_employee_user_id ON employee(user_id);
CREATE INDEX idx_project_repository_project_id ON project_repository(project_id);
CREATE INDEX idx_project_repository_file_repo_id ON project_repository_file(project_repository_id);
CREATE INDEX idx_task_info_project_id ON task_info(project_id);
CREATE INDEX idx_task_details_from_user ON task_details(from_user_id);
CREATE INDEX idx_task_details_to_user ON task_details(to_user_id);

COMMENT ON TABLE oauth_user IS 'Базовая таблица пользователей системы';
COMMENT ON TABLE user_info IS 'Дополнительная информация о пользователях';
COMMENT ON TABLE project IS 'Проекты IT планировщика';
COMMENT ON TABLE employee IS 'Сотрудники, привязанные к проектам';
COMMENT ON TABLE project_repository IS 'Репозитории файлов проектов';
COMMENT ON TABLE project_repository_file IS 'Файлы в репозиториях проектов';
COMMENT ON TABLE task_info IS 'Основная информация о задачах';
COMMENT ON TABLE task_details IS 'Детальная информация о задачах';
