DROP TYPE IF EXISTS role_enum CASCADE;

CREATE TYPE project_role_enum AS ENUM (
    'PROJECT_MANAGER',
    'FRONTEND_DEVELOPER',
    'BACKEND_DEVELOPER',
    'TESTER',
    'UI_UX_DESIGNER',
    'DEVOPS',
    'ANOTHER'
);

CREATE TYPE oauth_role_enum AS ENUM(
    'ADMIN',
    'USER'
);

ALTER TABLE oauth_user ADD COLUMN oauth_role oauth_role_enum DEFAULT 'USER';

ALTER TABLE employee ADD COLUMN project_role_temp project_role_enum;

UPDATE employee SET project_role_temp = role::text::project_role_enum;

ALTER TABLE employee DROP COLUMN role;

ALTER TABLE employee RENAME COLUMN project_role_temp TO project_role;

ALTER TABLE employee ALTER COLUMN project_role SET NOT NULL;