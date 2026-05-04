CREATE TABLE IF NOT EXISTS hammer.users (
    id                   UUID PRIMARY KEY,
    email                VARCHAR(256),
    nickname             VARCHAR(256) NOT NULL,
    status               SMALLINT NOT NULL DEFAULT 1,
    deleted_at           TIMESTAMPTZ,
    agreed_terms_version VARCHAR(20),
    created_at           TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ NOT NULL DEFAULT now()
);
