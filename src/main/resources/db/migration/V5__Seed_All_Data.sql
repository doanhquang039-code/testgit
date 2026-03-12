-- 1. Xóa sạch để đảm bảo ID bắt đầu từ 1
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE payroll;
TRUNCATE TABLE attendance;
TRUNCATE TABLE contract;
TRUNCATE TABLE user;
TRUNCATE TABLE department;
TRUNCATE TABLE jobposition;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. Chèn dữ liệu có ID cụ thể để đồng bộ khóa ngoại
INSERT INTO department (id, department_name, phone_number) VALUES 
(1, 'Phòng Nhân Sự', '0243.111.222'),
(2, 'Phòng Kỹ Thuật', '0243.333.444'),
(3, 'Phòng Kinh Doanh', '0243.555.666');

INSERT INTO jobposition (id, position_name, job_level, allowance_coeff) VALUES 
(1, 'Trưởng Phòng', 5, 1.5),
(2, 'Lập Trình Viên', 3, 1.2),
(3, 'Nhân Viên Kinh Doanh', 2, 1.0);

-- 3. Chèn User (Mật khẩu: 123456)
INSERT INTO user (id, username, password, full_name, email, role, department_id, position_id, status) VALUES 
(1, 'admin', '$2a$12$R9h/lZ9yWky.q.NInr0.9Ope75C9.S23Xv1BByq8Vd17Z5.S23Xv1', 'System Admin', 'admin@hrms.com', 'ADMIN', 1, 1, 'ACTIVE'),
(2, 'hoang.nv', '$2a$12$R9h/lZ9yWky.q.NInr0.9Ope75C9.S23Xv1BByq8Vd17Z5.S23Xv1', 'Nguyễn Văn Hoàng', 'hoang.nv@hrms.com', 'USER', 2, 2, 'ACTIVE'),
(3, 'lan.pt', '$2a$12$R9h/lZ9yWky.q.NInr0.9Ope75C9.S23Xv1BByq8Vd17Z5.S23Xv1', 'Phan Thị Lan', 'lan.pt@hrms.com', 'USER', 1, 3, 'ACTIVE');

-- 4. Chèn Attendance
INSERT INTO attendance (user_id, attendance_date, check_in_time, status, note) VALUES 
(1, '2026-03-11', '08:00:00', 'PRESENT', 'Admin check-in'),
(2, '2026-03-11', '08:45:00', 'LATE', 'Kẹt xe'),
(3, '2026-03-11', '08:05:00', 'PRESENT', 'Đúng giờ');

-- Cập nhật manager
UPDATE department SET manager_id = 1 WHERE id = 1;

-- Cực kỳ quan trọng: Lưu lại thay đổi
COMMIT;