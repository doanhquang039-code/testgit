-- ============================================================
-- V13: Tạo bảng Notification (Hệ thống thông báo nội bộ)
-- Lưu ý: Dùng IF NOT EXISTS vì Hibernate ddl-auto=update
--         có thể đã tự tạo bảng này khi khởi động ứng dụng.
-- ============================================================

CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL COMMENT 'Người nhận thông báo',
    message VARCHAR(300) NOT NULL COMMENT 'Nội dung thông báo',
    type VARCHAR(30) NOT NULL DEFAULT 'INFO'
        COMMENT 'INFO, SUCCESS, WARNING, DANGER, LEAVE_REQUEST, TASK_ASSIGNED, PAYROLL, ATTENDANCE',
    is_read BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Đã đọc chưa',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
    link VARCHAR(300) DEFAULT NULL COMMENT 'Đường dẫn liên quan (tùy chọn)',
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_notif_user (user_id),
    INDEX idx_notif_read (is_read),
    INDEX idx_notif_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='Bảng lưu thông báo nội bộ cho nhân viên';
