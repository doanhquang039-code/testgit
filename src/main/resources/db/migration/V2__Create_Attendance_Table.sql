-- Bảng Chấm công (Attendance)
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20) DEFAULT 'PRESENT', -- PRESENT, LATE, EARLY_LEAVE, ABSENT
    note TEXT,
    CONSTRAINT fk_attendance_user FOREIGN KEY (user_id) REFERENCES user(id),
    -- Đảm bảo mỗi nhân viên chỉ có 1 dòng chấm công mỗi ngày
    UNIQUE KEY unique_user_date (user_id, attendance_date)
);
