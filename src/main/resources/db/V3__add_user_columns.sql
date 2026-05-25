-- Migration: add new user columns
ALTER TABLE TB_ASMI_USER
  ADD COLUMN user_group VARCHAR(100),
  ADD COLUMN created_by VARCHAR(50),
  ADD COLUMN authorised_by VARCHAR(50),
  ADD COLUMN user_status VARCHAR(20),
  ADD COLUMN authorised_status CHAR(1);
