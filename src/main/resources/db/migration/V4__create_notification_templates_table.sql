CREATE TABLE IF NOT EXISTS auction.notification_templates (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    template_key   VARCHAR(128) NOT NULL UNIQUE,
    title_template VARCHAR(512) NOT NULL,
    body_template  VARCHAR(2048) NOT NULL,
    channel        VARCHAR(16) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
