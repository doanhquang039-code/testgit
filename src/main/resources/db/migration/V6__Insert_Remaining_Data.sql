-- 1. Chèn Hợp đồng (Contract) cho nhân viên đã có
-- Giả sử hoang.nv là ID 2, lan.pt là ID 3
INSERT INTO contract (user_id, contract_type, sign_date, expiry_date, base_salary_on_contract) VALUES 
(2, 'Hợp đồng dài hạn', '2026-01-01', '2029-01-01', 12000000.00),
(3, 'Hợp đồng thử việc', '2026-03-01', '2026-05-01', 9000000.00);

-- 2. Chèn danh mục Công việc (Task)
INSERT INTO task (task_name, description, task_type, max_participants, base_reward) VALUES 
('Thiết kế UI Dashboard', 'Vẽ mockup cho trang chủ quản trị', 'CREATIVE', 1, 1000000.00),
('Fix bug khóa ngoại', 'Xử lý lỗi SQL khi chạy Migration', 'TECHNICAL', 2, 500000.00);

-- 3. Phân công công việc (Task Assignment)
INSERT INTO taskassignment (user_id, task_id, assigned_date, status) VALUES 
(2, 2, '2026-03-11', 'IN_PROGRESS'),
(3, 1, '2026-03-11', 'PENDING');

-- 4. Chèn Yêu cầu nghỉ phép (Leave Request)
INSERT INTO leaverequest (user_id, leave_type, start_date, end_date, reason, status) VALUES 
(2, 'ANNUAL', '2026-03-15', '2026-03-16', 'Nghỉ việc gia đình', 'PENDING');

-- 5. Chèn tin nhắn Chatbot mẫu
INSERT INTO chatbotmessage (user_id, session_id, user_query, bot_response, intent) VALUES 
(2, 'session_99', 'Làm sao để đăng ký nghỉ phép?', 'Bạn vào mục Quản lý nghỉ phép và bấm Tạo yêu cầu.', 'GUIDE');

-- Lưu lại
COMMIT;