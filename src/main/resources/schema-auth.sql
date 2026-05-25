-- Schema for authentication module

CREATE TABLE IF NOT EXISTS TB_ASMI_USER (
  app_id VARCHAR(50),
  user_id VARCHAR(50) PRIMARY KEY,
  username VARCHAR(100) UNIQUE,
  password VARCHAR(256),
  role VARCHAR(50),
  email_id VARCHAR(100),
  phone_no VARCHAR(15),
  user_locked CHAR(1),
  password_fail_count INT,
  user_group VARCHAR(100),
  created_by VARCHAR(50),
  authorised_by VARCHAR(50),
  user_status VARCHAR(20),
  authorised_status CHAR(1),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS TB_ASMI_USER_SESSION (
  session_key VARCHAR(512) PRIMARY KEY,
  app_id VARCHAR(50),
  user_id VARCHAR(50),
  created_at TIMESTAMP,
  expire_at TIMESTAMP,
  refresh_token VARCHAR(200),
  refresh_expire_at TIMESTAMP,
  device_info VARCHAR(200),
  status VARCHAR(20)
);
