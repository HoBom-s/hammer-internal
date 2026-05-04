-- hammer-internal 초기 스키마
-- 실행: psql -h localhost -U hobom -d bear -f sql/init.sql

CREATE SCHEMA IF NOT EXISTS hammer;
CREATE SCHEMA IF NOT EXISTS auction;

-- users 테이블은 hammer-user 서비스가 소유.
-- 이미 존재하면 skip.
CREATE TABLE IF NOT EXISTS hammer.users (
    id              UUID PRIMARY KEY,
    email           VARCHAR(256),
    nickname        VARCHAR(256) NOT NULL,
    status          SMALLINT NOT NULL DEFAULT 1,
    deleted_at      TIMESTAMPTZ,
    agreed_terms_version VARCHAR(20),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS auction.quizzes (
    id              BIGSERIAL PRIMARY KEY,
    question        VARCHAR(500) NOT NULL,
    choice1         VARCHAR(200) NOT NULL,
    choice2         VARCHAR(200) NOT NULL,
    choice3         VARCHAR(200) NOT NULL,
    choice4         VARCHAR(200) NOT NULL,
    correct_index   INTEGER NOT NULL,
    explanation     VARCHAR(1000) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS auction.notification_templates (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    template_key    VARCHAR(128) NOT NULL UNIQUE,
    title_template  VARCHAR(512) NOT NULL,
    body_template   VARCHAR(2048) NOT NULL,
    channel         VARCHAR(16) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
