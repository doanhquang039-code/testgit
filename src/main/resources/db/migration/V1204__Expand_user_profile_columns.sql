-- Expand employee profile fields on `user` table
-- Keep idempotent checks for existing environments

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND COLUMN_NAME = 'employee_code'
);
SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE `user` ADD COLUMN employee_code VARCHAR(30) UNIQUE AFTER username',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND COLUMN_NAME = 'phone_number'
);
SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE `user` ADD COLUMN phone_number VARCHAR(20) AFTER email',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND COLUMN_NAME = 'gender'
);
SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE `user` ADD COLUMN gender VARCHAR(20) AFTER phone_number',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND COLUMN_NAME = 'date_of_birth'
);
SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE `user` ADD COLUMN date_of_birth DATE AFTER gender',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND COLUMN_NAME = 'address'
);
SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE `user` ADD COLUMN address VARCHAR(255) AFTER date_of_birth',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND COLUMN_NAME = 'hire_date'
);
SET @sql := IF(
    @col_exists = 0,
    'ALTER TABLE `user` ADD COLUMN hire_date DATE AFTER address',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(*)
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND INDEX_NAME = 'idx_user_employee_code'
);
SET @sql := IF(
    @idx_exists = 0,
    'CREATE INDEX idx_user_employee_code ON `user` (employee_code)',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
    SELECT COUNT(*)
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'user'
      AND INDEX_NAME = 'idx_user_phone_number'
);
SET @sql := IF(
    @idx_exists = 0,
    'CREATE INDEX idx_user_phone_number ON `user` (phone_number)',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
