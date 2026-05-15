-- Support encrypted audit detail payloads written by admin/manager/hiring mutation auditing.

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'hr_audit_log' AND COLUMN_NAME = 'detail_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE hr_audit_log ADD COLUMN detail_encrypted BIT(1) NOT NULL DEFAULT 0',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
