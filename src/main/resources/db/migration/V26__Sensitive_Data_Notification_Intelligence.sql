-- Sensitive HR data extensions and internal notification channels.

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'employee_profiles' AND COLUMN_NAME = 'identity_number'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE employee_profiles ADD COLUMN identity_number VARCHAR(1000) NULL AFTER bank_account_name',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'employee_profiles' AND COLUMN_NAME = 'bank_account_number'
);
SET @sql := IF(@col_exists > 0,
    'ALTER TABLE employee_profiles MODIFY bank_account_number VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'employee_profiles' AND COLUMN_NAME = 'tax_code'
);
SET @sql := IF(@col_exists > 0,
    'ALTER TABLE employee_profiles MODIFY tax_code VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'employee_profiles' AND COLUMN_NAME = 'social_insurance_number'
);
SET @sql := IF(@col_exists > 0,
    'ALTER TABLE employee_profiles MODIFY social_insurance_number VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'employee_profiles' AND COLUMN_NAME = 'health_insurance_number'
);
SET @sql := IF(@col_exists > 0,
    'ALTER TABLE employee_profiles MODIFY health_insurance_number VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'payroll' AND COLUMN_NAME = 'encrypted_salary_payload'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE payroll ADD COLUMN encrypted_salary_payload TEXT NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
