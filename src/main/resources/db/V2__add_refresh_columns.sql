-- Migration: add refresh token columns to session table
ALTER TABLE TB_ASMI_USER_SESSION
  ADD COLUMN refresh_token VARCHAR(200),
  ADD COLUMN refresh_expire_at TIMESTAMP;

-- optional: add index for faster lookup by refresh token
CREATE INDEX IF NOT EXISTS idx_refresh_token ON TB_ASMI_USER_SESSION(refresh_token);
