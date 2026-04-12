-- ============================================================
-- V14: Mở rộng dữ liệu mẫu để demo đầy đủ tính năng mới
-- Thêm: nhân viên, phòng ban, chức vụ, tasks, assignments,
--        lịch sử attendance, payroll tháng 4, thông báo mẫu
-- ============================================================

-- Tắt kiểm tra khóa ngoại tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- ========================= DEPARTMENTS =========================
INSERT IGNORE INTO department (id, department_name, phone_number) VALUES
(4, 'Phòng Marketing', '0243.777.888'),
(5, 'Phòng Kế Toán', '0243.999.000');

-- ========================= JOB POSITIONS =========================
INSERT IGNORE INTO jobposition (id, position_name, job_level, allowance_coeff, active) VALUES
(4, 'Giám Đốc', 7, 2.0, TRUE),
(5, 'Marketing Manager', 5, 1.6, TRUE),
(6, 'Kế Toán Trưởng', 5, 1.5, TRUE),
(7, 'Nhân Viên Marketing', 2, 1.1, TRUE),
(8, 'Nhân Viên Kế Toán', 2, 1.0, TRUE);

-- ========================= USERS (Mật khẩu: 123456) =========================
-- BCrypt hash của "123456"
INSERT IGNORE INTO user (id, username, password, full_name, email, role, department_id, position_id, status) VALUES
(4, 'minh.td',   '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Trần Đức Minh',   'minh.td@hrms.com',   'MANAGER', 2, 5, 'ACTIVE'),
(5, 'thu.lm',    '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Lê Minh Thu',     'thu.lm@hrms.com',    'USER',    5, 8, 'ACTIVE'),
(6, 'hung.nq',   '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Nguyễn Quốc Hùng','hung.nq@hrms.com',   'USER',    2, 2, 'ACTIVE'),
(7, 'linh.tv',   '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Trịnh Văn Linh',  'linh.tv@hrms.com',   'USER',    4, 7, 'ACTIVE'),
(8, 'mai.ht',    '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Hoàng Thị Mai',   'mai.ht@hrms.com',    'USER',    5, 8, 'ACTIVE'),
(9, 'son.pv',    '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Phạm Văn Sơn',    'son.pv@hrms.com',    'USER',    2, 2, 'ACTIVE'),
(10,'hoa.bk',   '$2a$10$hqVbLomRjVdJbGhyByGAeOYPaLYzGDxMIjilh3juV6.ZYc07DNkAu', 'Bùi Kim Hoa',     'hoa.bk@hrms.com',    'USER',    4, 7, 'ACTIVE');

-- Cập nhật manager cho phòng ban
UPDATE department SET manager_id = 4 WHERE id = 2;

-- ========================= HỢP ĐỒNG =========================
INSERT IGNORE INTO contract (user_id, contract_type, sign_date, expiry_date, base_salary_on_contract)
SELECT u.id, 'FULL_TIME', '2025-01-01', '2027-01-01', j.allowance_coeff * 10000000
FROM user u
JOIN jobposition j ON u.position_id = j.id
WHERE u.id IN (4,5,6,7,8,9,10)
ON DUPLICATE KEY UPDATE base_salary_on_contract = j.allowance_coeff * 10000000;

-- ========================= TASKS =========================
INSERT IGNORE INTO task (id, task_name, description, task_type, max_participants, base_reward, is_extra_shift) VALUES
(1, 'Phát triển Module Báo Cáo',     'Xây dựng module báo cáo tổng hợp theo tháng/quý/năm', 'PROJECT',      3, 2000000.00, FALSE),
(2, 'Triển khai CI/CD Pipeline',     'Thiết lập hệ thống tích hợp và triển khai liên tục',   'PROJECT',      2, 1500000.00, FALSE),
(3, 'Review Code Backend',            'Review và tối ưu code cho module nhân sự',              'REVIEW',       2, 500000.00,  FALSE),
(4, 'Họp Tổng Kết Quý 1/2026',       'Chuẩn bị báo cáo và tham dự họp quý',                  'MEETING',      8, 0.00,       FALSE),
(5, 'Thiết Kế UI Dashboard Mới',     'Thiết kế giao diện dashboard theo chuẩn Material',     'DESIGN',       2, 1000000.00, FALSE),
(6, 'Kiểm Thử API Tích Hợp',         'Testronic tất cả các API endpoint của hệ thống',        'TESTING',      2, 800000.00,  FALSE),
(7, 'Cập Nhật Tài Liệu Kỹ Thuật',   'Viết/cập nhật document cho toàn bộ API',               'DOCUMENTATION',1, 600000.00,  FALSE),
(8, 'Trực Hỗ Trợ Cuối Tuần',        'Hỗ trợ kỹ thuật cho khách hàng vào cuối tuần',         'SUPPORT',      1, 300000.00,  TRUE),
(9, 'Training Nhân Viên Mới',        'Hướng dẫn 3 nhân viên mới về quy trình làm việc',      'TRAINING',     2, 700000.00,  FALSE),
(10,'Audit Bảo Mật Hệ Thống',        'Kiểm tra toàn bộ lỗ hổng bảo mật hệ thống',            'SECURITY',     2, 1200000.00, FALSE);

-- ========================= TASK ASSIGNMENTS =========================
INSERT IGNORE INTO taskassignment (user_id, task_id, assigned_date, status, actual_hours) VALUES
-- Nguyễn Văn Hoàng (user 2)
(2,  1, '2026-04-01', 'IN_PROGRESS', 12.50),
(2,  4, '2026-04-06', 'COMPLETED',    2.00),
(2,  3, '2026-04-08', 'PENDING',      NULL),

-- Phan Thị Lan (user 3)
(3,  5, '2026-04-02', 'IN_PROGRESS',  8.00),
(3,  4, '2026-04-06', 'COMPLETED',    2.00),
(3,  9, '2026-04-10', 'PENDING',      NULL),

-- Trần Đức Minh (user 4 - Manager)
(4,  4, '2026-04-06', 'COMPLETED',    2.00),
(4, 10, '2026-04-07', 'IN_PROGRESS', 16.00),

-- Lê Minh Thu (user 5)
(5,  6, '2026-04-03', 'COMPLETED',   10.00),
(5,  7, '2026-04-09', 'IN_PROGRESS',  5.00),

-- Nguyễn Quốc Hùng (user 6)
(6,  2, '2026-04-01', 'IN_PROGRESS', 20.00),
(6,  3, '2026-04-08', 'PENDING',      NULL),

-- Trịnh Văn Linh (user 7)
(7,  5, '2026-04-02', 'IN_PROGRESS',  6.00),
(7,  8, '2026-04-05', 'COMPLETED',    8.00),

-- Hoàng Thị Mai (user 8)
(8,  6, '2026-04-03', 'COMPLETED',   10.00),
(8,  9, '2026-04-10', 'PENDING',      NULL),

-- Phạm Văn Sơn (user 9)
(9,  1, '2026-04-01', 'IN_PROGRESS', 14.00),
(9,  2, '2026-04-01', 'IN_PROGRESS', 18.00),

-- Bùi Kim Hoa (user 10)
(10, 7, '2026-04-09', 'PENDING',      NULL),
(10, 4, '2026-04-06', 'COMPLETED',    2.00);

-- ========================= ATTENDANCE (Tháng 4/2026) =========================
-- Chèn dữ liệu chấm công cho các ngày làm việc tháng 4/2026
-- user_id 2 = Nguyễn Văn Hoàng
INSERT IGNORE INTO attendance (user_id, attendance_date, check_in_time, check_out_time, status, note) VALUES
(2, '2026-04-01', '08:02:00', '17:35:00', 'PRESENT',     NULL),
(2, '2026-04-02', '08:45:00', '17:30:00', 'LATE',        'Kẹt xe'),
(2, '2026-04-03', '07:55:00', '17:32:00', 'PRESENT',     NULL),
(2, '2026-04-04', '08:00:00', '17:00:00', 'PRESENT',     NULL),
(2, '2026-04-07', '08:30:00', '17:31:00', 'PRESENT',     NULL),
(2, '2026-04-08', '08:10:00', '16:45:00', 'EARLY_LEAVE', 'Đi khám bệnh'),
(2, '2026-04-09', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(2, '2026-04-10', '08:05:00', '17:25:00', 'PRESENT',     NULL),
(2, '2026-04-11', '09:00:00', '17:30:00', 'LATE',        'Xe hỏng'),

-- user_id 3 = Phan Thị Lan
(3, '2026-04-01', '07:58:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-02', '08:01:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-03', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-04', '08:15:00', '17:00:00', 'PRESENT',     NULL),
(3, '2026-04-07', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-08', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-09', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-10', '08:30:00', '17:30:00', 'PRESENT',     NULL),
(3, '2026-04-11', '08:00:00', '17:00:00', 'PRESENT',     NULL),

-- user_id 5 = Lê Minh Thu
(5, '2026-04-01', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(5, '2026-04-02', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(5, '2026-04-03', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(5, '2026-04-07', '08:55:00', '17:30:00', 'LATE',        'Máy tính bị lỗi'),
(5, '2026-04-08', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(5, '2026-04-09', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(5, '2026-04-10', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(5, '2026-04-11', '08:00:00', '17:30:00', 'PRESENT',     NULL),

-- user_id 6 = Nguyễn Quốc Hùng
(6, '2026-04-01', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(6, '2026-04-02', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(6, '2026-04-03', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(6, '2026-04-07', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(6, '2026-04-08', '08:00:00', '17:30:00', 'PRESENT',     NULL),
(6, '2026-04-11', '08:00:00', '17:30:00', 'PRESENT',     NULL);

-- ========================= PAYROLL THÁNG 4/2026 =========================
INSERT IGNORE INTO payroll (user_id, month, year, base_salary, bonus, deductions, payment_status) VALUES
(2,  4, 2026, 15000000.00, 1500000.00, 300000.00, 'PENDING'),
(3,  4, 2026, 10000000.00,  500000.00,       0.00, 'PENDING'),
(4,  4, 2026, 25000000.00, 3000000.00,       0.00, 'PAID'),
(5,  4, 2026, 12000000.00, 1000000.00,  200000.00, 'PENDING'),
(6,  4, 2026, 15000000.00, 2000000.00,       0.00, 'PENDING'),
(7,  4, 2026, 11000000.00,  800000.00,       0.00, 'PENDING'),
(8,  4, 2026, 12000000.00,  700000.00,  150000.00, 'PENDING'),
(9,  4, 2026, 15000000.00, 2800000.00,       0.00, 'PENDING'),
(10, 4, 2026, 11000000.00,  600000.00,       0.00, 'PENDING');

-- ========================= LEAVE REQUESTS =========================
INSERT IGNORE INTO leaverequest (user_id, leave_type, start_date, end_date, reason, status) VALUES
(2, 'ANNUAL',   '2026-04-14', '2026-04-15', 'Nghỉ phép năm, đi du lịch cùng gia đình',        'PENDING'),
(3, 'SICK',     '2026-04-10', '2026-04-11', 'Bị cảm cúm, cần nghỉ điều trị',                  'APPROVED'),
(5, 'ANNUAL',   '2026-04-20', '2026-04-20', 'Nghỉ giải quyết việc cá nhân',                   'PENDING'),
(6, 'MATERNITY','2026-05-01', '2026-07-31', 'Nghỉ thai sản theo quy định',                    'PENDING'),
(7, 'UNPAID',   '2026-04-17', '2026-04-18', 'Về quê giỗ tổ',                                  'APPROVED'),
(8, 'SICK',     '2026-04-08', '2026-04-09', 'Đau đầu dữ dội, cần nghỉ ngơi',                  'REJECTED'),
(9, 'ANNUAL',   '2026-04-28', '2026-04-30', 'Kỳ nghỉ lễ 30/4',                                'PENDING'),
(10,'ANNUAL',   '2026-04-28', '2026-04-30', 'Kỳ nghỉ lễ 30/4 - 1/5',                          'PENDING');

-- ========================= NOTIFICATIONS MẪU =========================
INSERT IGNORE INTO notification (user_id, message, type, is_read, created_at, link) VALUES
-- Cho user 2 (hoang.nv)
(2, '📝 Bạn được giao công việc mới: "Phát triển Module Báo Cáo". Hãy kiểm tra ngay!',       'TASK_ASSIGNED',  FALSE, '2026-04-01 08:30:00', '/user1/tasks'),
(2, '📝 Bạn được giao công việc mới: "Review Code Backend". Deadline cuối tháng!',            'TASK_ASSIGNED',  FALSE, '2026-04-08 09:00:00', '/user1/tasks'),
(2, '📅 Đơn xin nghỉ ANNUAL từ 2026-04-14 đến 2026-04-15 đã được gửi, đang chờ duyệt.',    'LEAVE_REQUEST',  TRUE,  '2026-04-12 10:00:00', '/user/leaves'),
(2, '💰 Phiếu lương tháng 4/2026 đã được tạo. Hãy kiểm tra ngay!',                          'PAYROLL',        FALSE, '2026-04-05 08:00:00', '/user1/payroll'),

-- Cho user 3 (lan.pt)  
(3, '✅ Đơn nghỉ phép SICK từ 2026-04-10 đến 2026-04-11 đã được DUYỆT!',                    'SUCCESS',        FALSE, '2026-04-09 16:30:00', '/user/leaves'),
(3, '📝 Bạn được giao công việc mới: "Thiết Kế UI Dashboard Mới".',                          'TASK_ASSIGNED',  TRUE,  '2026-04-02 08:30:00', '/user1/tasks'),
(3, '💰 Phiếu lương tháng 4/2026 đã được tạo. Hãy kiểm tra ngay!',                          'PAYROLL',        FALSE, '2026-04-05 08:00:00', '/user1/payroll'),

-- Cho user 5 (thu.lm)
(5, '📝 Bạn được giao công việc mới: "Kiểm Thử API Tích Hợp".',                             'TASK_ASSIGNED',  TRUE,  '2026-04-03 08:30:00', '/user1/tasks'),
(5, '📝 Bạn được giao công việc mới: "Cập Nhật Tài Liệu Kỹ Thuật".',                        'TASK_ASSIGNED',  FALSE, '2026-04-09 09:00:00', '/user1/tasks'),
(5, '❌ Đơn nghỉ phép SICK từ 2026-04-08 đã bị TỪ CHỐI.',                                  'DANGER',         FALSE, '2026-04-07 17:00:00', '/user/leaves'),
(5, '💰 Lương tháng 4/2026 đã được thanh toán toàn bộ!',                                     'SUCCESS',        FALSE, '2026-04-12 08:00:00', '/user1/payroll'),

-- Cho user 6 (hung.nq)
(6, '📝 Bạn được giao công việc mới: "Triển khai CI/CD Pipeline".',                          'TASK_ASSIGNED',  TRUE,  '2026-04-01 08:30:00', '/user1/tasks'),
(6, '📅 Đơn xin nghỉ MATERNITY từ 2026-05-01 đến 2026-07-31 đã được gửi, đang chờ duyệt.','LEAVE_REQUEST',   FALSE, '2026-04-10 09:00:00', '/user/leaves'),
(6, '💰 Phiếu lương tháng 4/2026 đã được tạo. Hãy kiểm tra ngay!',                          'PAYROLL',        FALSE, '2026-04-05 08:00:00', '/user1/payroll');

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

COMMIT;
