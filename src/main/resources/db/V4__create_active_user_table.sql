-- Migration: create active user table to maintain active sessions
CREATE TABLE IF NOT EXISTS TB_ASMI_ACTIVE_USER (
  session_key VARCHAR(512) PRIMARY KEY,
  app_id VARCHAR(50),
  user_id VARCHAR(50),
  username VARCHAR(100),
  role VARCHAR(50),
  login_at TIMESTAMP,
  expire_at TIMESTAMP,
  device_info VARCHAR(200),
  status VARCHAR(20)
);

CREATE INDEX IF NOT EXISTS idx_active_user_role ON TB_ASMI_ACTIVE_USER(role);
