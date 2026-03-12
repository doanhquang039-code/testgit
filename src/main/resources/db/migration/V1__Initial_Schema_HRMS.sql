-- 1. Bảng Chức Vụ (Tạo trước vì không phụ thuộc ai)
CREATE TABLE IF NOT EXISTS jobposition (
    id INT AUTO_INCREMENT PRIMARY KEY,
    position_name VARCHAR(100) NOT NULL,
    job_level INT DEFAULT 1,
    allowance_coeff DECIMAL(5, 2) DEFAULT 0.00,
    next_position_id INT,
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_next_position FOREIGN KEY (next_position_id) REFERENCES jobposition(id)
);

-- 2. Bảng Phòng Ban (Tạo khung trước)
CREATE TABLE IF NOT EXISTS department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15),
    manager_id INT,
    parent_department_id INT -- Đã có dấu phẩy ở trên, không còn lỗi dòng 9
);

-- 3. Bảng Người Dùng
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    profile_image VARCHAR(255) DEFAULT 'default_avatar.png',
    role VARCHAR(20) DEFAULT 'USER',
    department_id INT,
    position_id INT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    CONSTRAINT fk_user_dept FOREIGN KEY (department_id) REFERENCES department(id),
    CONSTRAINT fk_user_pos FOREIGN KEY (position_id) REFERENCES jobposition(id)
);

-- 4. Thêm các ràng buộc khóa ngoại cho Department (Sau khi đã có bảng User)
ALTER TABLE department ADD CONSTRAINT fk_dept_manager FOREIGN KEY (manager_id) REFERENCES user(id);
ALTER TABLE department ADD CONSTRAINT fk_dept_parent FOREIGN KEY (parent_department_id) REFERENCES department(id);

-- 5. Bảng Hợp Đồng
CREATE TABLE IF NOT EXISTS contract (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    contract_type VARCHAR(50),
    sign_date DATE,
    expiry_date DATE,
    base_salary_on_contract DECIMAL(15, 2),
    contract_image VARCHAR(255),
    CONSTRAINT fk_contract_user FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 6. Bảng Công Việc
CREATE TABLE IF NOT EXISTS task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    description TEXT,
    task_type VARCHAR(20) NOT NULL,
    max_participants INT DEFAULT 1,
    base_reward DECIMAL(15, 2) DEFAULT 0.00,
    is_extra_shift BOOLEAN DEFAULT FALSE
);

-- 7. Bảng Phân Công Công Việc
CREATE TABLE IF NOT EXISTS taskassignment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    task_id INT,
    assigned_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    actual_hours DECIMAL(5, 2),
    CONSTRAINT fk_assign_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_assign_task FOREIGN KEY (task_id) REFERENCES task(id)
);

-- 8. Bảng Yêu Cầu Nghỉ Phép
CREATE TABLE IF NOT EXISTS leaverequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    leave_type VARCHAR(20) DEFAULT 'ANNUAL',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    approved_by INT,
    CONSTRAINT fk_leave_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_leave_admin FOREIGN KEY (approved_by) REFERENCES user(id)
);

-- 9. Bảng Lương
CREATE TABLE IF NOT EXISTS payroll (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    month INT,
    year INT,
    base_salary DECIMAL(15, 2),
    bonus DECIMAL(15, 2) DEFAULT 0.00,
    deductions DECIMAL(15, 2) DEFAULT 0.00,
    net_salary DECIMAL(15, 2) AS (base_salary + bonus - deductions) STORED,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    CONSTRAINT fk_payroll_user FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 10. Bảng Chatbot Message
CREATE TABLE IF NOT EXISTS chatbotmessage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    session_id VARCHAR(255),
    user_query TEXT,
    bot_response TEXT,
    intent VARCHAR(100),
    rating INT,
    is_escalated BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chat_user FOREIGN KEY (user_id) REFERENCES user(id)
);