-- =====================================================
-- Migration V22: Create QR Code, OKR, and System Settings Tables
-- =====================================================

-- =====================================================
-- QR CODE TABLES
-- =====================================================

CREATE TABLE IF NOT EXISTS qr_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(30) NOT NULL,
    name VARCHAR(200),
    description TEXT,
    location VARCHAR(200),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    scan_count INT NOT NULL DEFAULT 0,
    last_scanned_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    expires_at DATETIME,
    metadata TEXT,
    INDEX idx_qr_code (code),
    INDEX idx_qr_type (type),
    INDEX idx_qr_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS qr_code_scans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    qr_code_id INT NOT NULL,
    user_id INT NOT NULL,
    scan_type VARCHAR(30) DEFAULT 'SCAN',
    scanned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    device_info VARCHAR(500),
    is_successful BOOLEAN NOT NULL DEFAULT TRUE,
    error_message VARCHAR(500),
    location_lat DECIMAL(10, 8),
    location_lng DECIMAL(11, 8),
    notes TEXT,
    FOREIGN KEY (qr_code_id) REFERENCES qr_codes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_scan_qrcode (qr_code_id),
    INDEX idx_scan_user (user_id),
    INDEX idx_scan_date (scanned_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- OKR (Objectives and Key Results) TABLES
-- =====================================================

CREATE TABLE IF NOT EXISTS objectives (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    level VARCHAR(30) NOT NULL,
    owner_id INT,
    department_id INT,
    parent_objective_id BIGINT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    progress DOUBLE NOT NULL DEFAULT 0.0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL,
    FOREIGN KEY (parent_objective_id) REFERENCES objectives(id) ON DELETE SET NULL,
    INDEX idx_objective_owner (owner_id),
    INDEX idx_objective_dept (department_id),
    INDEX idx_objective_status (status),
    INDEX idx_objective_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS key_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    objective_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    metric_type VARCHAR(30) NOT NULL,
    start_value DOUBLE NOT NULL DEFAULT 0.0,
    target_value DOUBLE NOT NULL,
    current_value DOUBLE NOT NULL DEFAULT 0.0,
    unit VARCHAR(50),
    progress DOUBLE NOT NULL DEFAULT 0.0,
    weight DOUBLE NOT NULL DEFAULT 1.0,
    status VARCHAR(30) NOT NULL DEFAULT 'NOT_STARTED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (objective_id) REFERENCES objectives(id) ON DELETE CASCADE,
    INDEX idx_kr_objective (objective_id),
    INDEX idx_kr_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS key_result_updates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    key_result_id BIGINT NOT NULL,
    previous_value DOUBLE NOT NULL,
    new_value DOUBLE NOT NULL,
    notes TEXT,
    updated_by INT NOT NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (key_result_id) REFERENCES key_results(id) ON DELETE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_kru_keyresult (key_result_id),
    INDEX idx_kru_date (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- SYSTEM SETTINGS TABLE
-- =====================================================

CREATE TABLE IF NOT EXISTS system_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    setting_type VARCHAR(30) NOT NULL DEFAULT 'STRING',
    category VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
    description VARCHAR(500),
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    is_editable BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by INT,
    FOREIGN KEY (updated_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_setting_key (setting_key),
    INDEX idx_setting_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tables already exist, migration complete
