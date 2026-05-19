SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'user' AND COLUMN_NAME = 'cccd'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE user ADD COLUMN cccd VARCHAR(1000) NULL AFTER phone_number',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
