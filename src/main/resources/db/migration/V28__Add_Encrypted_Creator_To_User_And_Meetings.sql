-- Store creator/updater identity as encrypted text only. No plain actor names are persisted.

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'created_by_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE user ADD COLUMN created_by_encrypted VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'updated_by_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE user ADD COLUMN updated_by_encrypted VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'meetings' AND COLUMN_NAME = 'created_by_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE meetings ADD COLUMN created_by_encrypted VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'meetings' AND COLUMN_NAME = 'updated_by_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE meetings ADD COLUMN updated_by_encrypted VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
