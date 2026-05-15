-- Bổ sung thông tin quản lý phòng ban, thời gian làm việc và audit người thao tác đã mã hóa.

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'department' AND COLUMN_NAME = 'employee_count'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE department ADD COLUMN employee_count INT NOT NULL DEFAULT 0',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'department' AND COLUMN_NAME = 'working_hours'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE department ADD COLUMN working_hours VARCHAR(100) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'department' AND COLUMN_NAME = 'created_by_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE department ADD COLUMN created_by_encrypted VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists := (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'department' AND COLUMN_NAME = 'updated_by_encrypted'
);
SET @sql := IF(@col_exists = 0,
    'ALTER TABLE department ADD COLUMN updated_by_encrypted VARCHAR(1000) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE department d
LEFT JOIN (
    SELECT department_id, COUNT(*) AS total_employee
    FROM user
    WHERE department_id IS NOT NULL
    GROUP BY department_id
) u ON u.department_id = d.id
SET d.employee_count = COALESCE(u.total_employee, 0);
