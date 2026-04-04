CREATE TABLE ai_response
(
    id              BIGSERIAL PRIMARY KEY,
    response        TEXT   NOT NULL,
    processing_time BIGINT NOT NULL,
    request_id      BIGINT NOT NULL REFERENCES ai_request (id) ON DELETE CASCADE
);
