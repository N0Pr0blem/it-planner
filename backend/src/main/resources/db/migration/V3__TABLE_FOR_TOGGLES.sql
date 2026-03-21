CREATE TABLE feature_toggles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(32),
    description VARCHAR(256),
    enabled     BOOLEAN default false
);

INSERT INTO feature_toggles
values (1, 'email.sending', 'Activate sending verification code', false);