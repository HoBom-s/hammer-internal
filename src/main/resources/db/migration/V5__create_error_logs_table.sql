CREATE TABLE IF NOT EXISTS hammer.error_logs (
    id           BIGSERIAL PRIMARY KEY,
    method       VARCHAR(10) NOT NULL,
    uri          VARCHAR(2048) NOT NULL,
    status       INTEGER NOT NULL,
    error_code   VARCHAR(64),
    message      TEXT,
    stack_trace  TEXT,
    request_body TEXT,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_error_logs_created_at ON hammer.error_logs(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_error_logs_status ON hammer.error_logs(status);
