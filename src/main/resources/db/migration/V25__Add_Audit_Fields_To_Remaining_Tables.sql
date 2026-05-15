-- V25__Add_Audit_Fields_To_Remaining_Tables.sql

ALTER TABLE company_announcement ADD COLUMN updated_at DATETIME;
ALTER TABLE company_asset ADD COLUMN updated_at DATETIME;
ALTER TABLE employee_benefit ADD COLUMN updated_at DATETIME;
ALTER TABLE employee_warning ADD COLUMN updated_at DATETIME;
ALTER TABLE hr_audit_log ADD COLUMN updated_at DATETIME;
ALTER TABLE leaverequest ADD COLUMN updated_at DATETIME;
ALTER TABLE notification ADD COLUMN updated_at DATETIME;
ALTER TABLE okr_progress ADD COLUMN updated_at DATETIME;
ALTER TABLE qr_codes ADD COLUMN updated_at DATETIME;
ALTER TABLE recognitions ADD COLUMN updated_at DATETIME;
ALTER TABLE shift_assignments ADD COLUMN updated_at DATETIME;
ALTER TABLE training_program ADD COLUMN updated_at DATETIME;

-- Missing Create
ALTER TABLE candidates ADD COLUMN created_at DATETIME;
ALTER TABLE system_setting ADD COLUMN created_at DATETIME;
