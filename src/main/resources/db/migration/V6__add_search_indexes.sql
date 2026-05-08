-- Quiz keyword search
CREATE INDEX IF NOT EXISTS idx_quizzes_question ON auction.quizzes (question);

-- Notification template filters
CREATE INDEX IF NOT EXISTS idx_notification_templates_channel ON auction.notification_templates (channel);

-- Error log advanced filters
CREATE INDEX IF NOT EXISTS idx_error_logs_error_code ON hammer.error_logs (error_code);
CREATE INDEX IF NOT EXISTS idx_error_logs_uri ON hammer.error_logs (uri);
CREATE INDEX IF NOT EXISTS idx_error_logs_created_at ON hammer.error_logs (created_at);
