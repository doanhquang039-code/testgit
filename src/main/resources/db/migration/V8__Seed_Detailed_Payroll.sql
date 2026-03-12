-- 1. Chèn dữ liệu bảng Payroll cho tháng 03/2026
-- Giả sử:
-- user_id 1: Admin (Lương 30tr)
-- user_id 2: hoang.nv (Lương 15tr + Thưởng dự án 1tr ở V7)
-- user_id 3: lan.pt (Lương 10tr - Phạt 200k ở V7)

INSERT INTO payroll (user_id, month, year, base_salary, bonus, deductions, payment_status) VALUES 
(1, 3, 2026, 30000000.00, 2000000.00, 0.00, 'PAID'),
(2, 3, 2026, 15000000.00, 1000000.00, 0.00, 'PENDING'),
(3, 3, 2026, 10000000.00, 0.00, 200000.00, 'PENDING');

-- 2. Chèn bổ sung lương tháng 1 & 2 để làm báo cáo biểu đồ (Lịch sử lương)
INSERT INTO payroll (user_id, month, year, base_salary, bonus, deductions, payment_status) VALUES 
(1, 1, 2026, 30000000.00, 5000000.00, 0.00, 'PAID'),
(2, 1, 2026, 15000000.00, 500000.00, 100000.00, 'PAID'),
(3, 1, 2026, 10000000.00, 200000.00, 0.00, 'PAID'),
(1, 2, 2026, 30000000.00, 1000000.00, 0.00, 'PAID'),
(2, 2, 2026, 15000000.00, 800000.00, 0.00, 'PAID'),
(3, 2, 2026, 10000000.00, 0.00, 500000.00, 'PAID');

-- 3. Cập nhật ghi chú hoặc thông tin bổ sung nếu bảng của bạn có cột note (tùy chọn)
-- Nếu bảng payroll của bạn chưa có cột thực nhận tự động tính, 
-- MySQL sẽ tự tính cột net_salary dựa trên công thức bạn đã định nghĩa ở V1.

-- Lưu lại thay đổi
COMMIT;