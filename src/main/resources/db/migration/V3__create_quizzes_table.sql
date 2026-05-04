CREATE TABLE IF NOT EXISTS auction.quizzes (
    id            BIGSERIAL PRIMARY KEY,
    question      VARCHAR(500) NOT NULL,
    choice1       VARCHAR(200) NOT NULL,
    choice2       VARCHAR(200) NOT NULL,
    choice3       VARCHAR(200) NOT NULL,
    choice4       VARCHAR(200) NOT NULL,
    correct_index INTEGER NOT NULL,
    explanation   VARCHAR(1000) NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
