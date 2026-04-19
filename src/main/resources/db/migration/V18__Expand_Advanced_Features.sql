-- =====================================================
-- V18: Expand Advanced Features
-- Thêm 8 bảng mới cho các tính năng nâng cao
-- =====================================================

-- 1. Employee Documents (Tài liệu nhân viên)
CREATE TABLE IF NOT EXISTS employee_document (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    document_type   VARCHAR(30) NOT NULL DEFAULT 'OTHER',
    document_name   VARCHAR(200) NOT NULL,
    file_url        VARCHAR(500),
    file_size       BIGINT DEFAULT 0,
    mime_type       VARCHAR(100),
    description     TEXT,
    uploaded_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry_date     DATE,
    is_verified     BOOLEAN DEFAULT FALSE,
    verified_by     INT,
    verified_at     DATETIME,
    CONSTRAINT fk_doc_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_doc_verified_by FOREIGN KEY (verified_by) REFERENCES user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Overtime Requests (Đăng ký làm thêm giờ)
CREATE TABLE IF NOT EXISTS overtime_request (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    overtime_date   DATE NOT NULL,
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    total_hours     DECIMAL(5,2) NOT NULL DEFAULT 0,
    multiplier      DECIMAL(3,1) NOT NULL DEFAULT 1.5,
    reason          TEXT,
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    approved_by     INT,
    approved_at     DATETIME,
    rejection_reason TEXT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ot_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_ot_approved_by FOREIGN KEY (approved_by) REFERENCES user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Training Programs (Chương trình đào tạo)
CREATE TABLE IF NOT EXISTS training_program (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    program_name    VARCHAR(200) NOT NULL,
    description     TEXT,
    instructor      VARCHAR(100),
    department_id   INT,
    start_date      DATE,
    end_date        DATE,
    max_capacity    INT DEFAULT 30,
    location        VARCHAR(200),
    training_type   VARCHAR(30) DEFAULT 'INTERNAL',
    status          VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
    budget          DECIMAL(15,2) DEFAULT 0,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tp_department FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Training Enrollments (Ghi danh đào tạo)
CREATE TABLE IF NOT EXISTS training_enrollment (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    program_id      INT NOT NULL,
    enrolled_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    score           DECIMAL(5,2),
    feedback        TEXT,
    certificate_url VARCHAR(500),
    completed_at    DATETIME,
    CONSTRAINT fk_te_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_te_program FOREIGN KEY (program_id) REFERENCES training_program(id) ON DELETE CASCADE,
    CONSTRAINT uk_enrollment UNIQUE (user_id, program_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Employee Warnings (Cảnh cáo / Kỷ luật)
CREATE TABLE IF NOT EXISTS employee_warning (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    issued_by       INT NOT NULL,
    warning_level   VARCHAR(20) NOT NULL DEFAULT 'VERBAL',
    reason          TEXT NOT NULL,
    description     TEXT,
    issued_date     DATE NOT NULL,
    expiry_date     DATE,
    is_acknowledged BOOLEAN DEFAULT FALSE,
    acknowledged_at DATETIME,
    attachment_url  VARCHAR(500),
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ew_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_ew_issued_by FOREIGN KEY (issued_by) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Employee Benefits (Phúc lợi nhân viên)
CREATE TABLE IF NOT EXISTS employee_benefit (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT NOT NULL,
    benefit_type    VARCHAR(30) NOT NULL,
    benefit_name    VARCHAR(200) NOT NULL,
    description     TEXT,
    monetary_value  DECIMAL(15,2) DEFAULT 0,
    start_date      DATE NOT NULL,
    end_date        DATE,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    provider        VARCHAR(200),
    policy_number   VARCHAR(100),
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eb_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Company Assets (Tài sản công ty)
CREATE TABLE IF NOT EXISTS company_asset (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    asset_name      VARCHAR(200) NOT NULL,
    asset_code      VARCHAR(50) UNIQUE,
    category        VARCHAR(50) NOT NULL,
    serial_number   VARCHAR(100),
    description     TEXT,
    purchase_date   DATE,
    purchase_price  DECIMAL(15,2) DEFAULT 0,
    current_value   DECIMAL(15,2) DEFAULT 0,
    status          VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    location        VARCHAR(200),
    warranty_expiry DATE,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Asset Assignments (Gán tài sản cho nhân viên)
CREATE TABLE IF NOT EXISTS asset_assignment (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    asset_id        INT NOT NULL,
    user_id         INT NOT NULL,
    assigned_date   DATE NOT NULL,
    expected_return DATE,
    actual_return   DATE,
    assigned_by     INT,
    condition_on_assign VARCHAR(50) DEFAULT 'GOOD',
    condition_on_return VARCHAR(50),
    notes           TEXT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_aa_asset FOREIGN KEY (asset_id) REFERENCES company_asset(id) ON DELETE CASCADE,
    CONSTRAINT fk_aa_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_aa_assigned_by FOREIGN KEY (assigned_by) REFERENCES user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- Indexes for performance
-- =====================================================
CREATE INDEX idx_doc_user ON employee_document(user_id);
CREATE INDEX idx_doc_type ON employee_document(document_type);
CREATE INDEX idx_ot_user ON overtime_request(user_id);
CREATE INDEX idx_ot_date ON overtime_request(overtime_date);
CREATE INDEX idx_ot_status ON overtime_request(status);
CREATE INDEX idx_tp_status ON training_program(status);
CREATE INDEX idx_tp_dept ON training_program(department_id);
CREATE INDEX idx_te_user ON training_enrollment(user_id);
CREATE INDEX idx_te_program ON training_enrollment(program_id);
CREATE INDEX idx_ew_user ON employee_warning(user_id);
CREATE INDEX idx_ew_level ON employee_warning(warning_level);
CREATE INDEX idx_eb_user ON employee_benefit(user_id);
CREATE INDEX idx_eb_type ON employee_benefit(benefit_type);
CREATE INDEX idx_eb_status ON employee_benefit(status);
CREATE INDEX idx_ca_status ON company_asset(status);
CREATE INDEX idx_ca_category ON company_asset(category);
CREATE INDEX idx_aa_asset ON asset_assignment(asset_id);
CREATE INDEX idx_aa_user ON asset_assignment(user_id);

-- =====================================================
-- Insert sample data
-- =====================================================

-- Sample Training Programs
INSERT INTO training_program (program_name, description, instructor, start_date, end_date, max_capacity, location, training_type, status, budget)
VALUES
('Java Spring Boot Advanced', 'Khóa đào tạo nâng cao Spring Boot & Microservices', 'Nguyễn Văn An', '2026-05-01', '2026-05-15', 25, 'Phòng đào tạo A3', 'INTERNAL', 'PLANNED', 5000000),
('Leadership & Management', 'Kỹ năng lãnh đạo và quản lý đội nhóm', 'Trần Thị Bình', '2026-05-10', '2026-05-12', 20, 'Phòng họp chính', 'INTERNAL', 'PLANNED', 3000000),
('Cybersecurity Fundamentals', 'Bảo mật thông tin cơ bản', 'Security Corp.', '2026-06-01', '2026-06-05', 30, 'Online', 'EXTERNAL', 'PLANNED', 10000000);

-- Sample Company Assets
INSERT INTO company_asset (asset_name, asset_code, category, serial_number, purchase_date, purchase_price, current_value, status, location)
VALUES
('MacBook Pro 14" M3', 'LAP-001', 'LAPTOP', 'C02FN3XXXX', '2025-01-15', 45000000, 38000000, 'ASSIGNED', 'Văn phòng HCM'),
('MacBook Pro 14" M3', 'LAP-002', 'LAPTOP', 'C02FN3YYYY', '2025-01-15', 45000000, 38000000, 'AVAILABLE', 'Kho IT'),
('Dell UltraSharp 27"', 'MON-001', 'MONITOR', 'DELL27XXXX', '2025-03-01', 12000000, 10000000, 'ASSIGNED', 'Văn phòng HCM'),
('Ergonomic Desk', 'DSK-001', 'FURNITURE', 'DESK-ERG-001', '2024-06-01', 8000000, 6000000, 'ASSIGNED', 'Văn phòng HCM'),
('iPhone 15 Pro', 'PHN-001', 'PHONE', 'IPHXXXX', '2025-06-01', 28000000, 24000000, 'AVAILABLE', 'Kho IT');
