Markdown
# HR Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Build with Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)](https://www.docker.com/)
[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?logo=springboot)](https://spring.io/)
[![Frontend](https://img.shields.io/badge/Frontend-React-61DAFB?logo=react)](https://react.dev/)

> Hệ thống quản lý nhân sự toàn diện giúp doanh nghiệp số hóa quy trình HR: quản lý hồ sơ nhân viên, phòng ban, chấm công, tính lương và báo cáo.

**Repo:** https://github.com/doanhquang039-code/hr-managerment-system

## Mục lục
- [Tổng quan](#tổng-quan)
- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt và Chạy](#cài-đặt-và-chạy)
- [Biến môi trường](#biến-môi-trường)
- [API Documentation](#api-documentation)
- [Đóng góp](#đóng-góp)
- [License](#license)

## Tổng quan
HR Management System là ứng dụng web full-stack được xây dựng để tự động hóa các nghiệp vụ quản lý nhân sự. Hệ thống tách riêng backend và frontend, giao tiếp qua RESTful API và hỗ trợ triển khai nhanh bằng Docker.

## Tính năng
| Module | Mô tả chi tiết |
| --- | --- |
| **Quản lý nhân viên** | CRUD hồ sơ nhân viên, tìm kiếm/lọc, upload avatar, import Excel |
| **Quản lý phòng ban** | Sơ đồ tổ chức, gán trưởng phòng, điều chuyển nhân sự |
| **Chấm công** | Check-in/out, quản lý ca làm, duyệt đơn nghỉ phép |
| **Tính lương** | Cấu hình lương cơ bản, phụ cấp, khấu trừ, xuất bảng lương PDF |
| **Phân quyền** | RBAC với 4 roles: Admin, HR, Manager, Employee |
| **Dashboard & Báo cáo** | Thống kê nhân sự, biểu đồ biến động, xuất Excel |
| **Xác thực** | JWT Authentication, refresh token, phân quyền API |

> Lưu ý: Tick vào các module bạn đã hoàn thành, bỏ hoặc thêm mới tùy theo code hiện tại.

## Công nghệ sử dụng

### Backend
- **Java 17** + **Spring Boot 3.x**
- **Spring Security + JWT** - Xác thực & phân quyền
- **Spring Data JPA / Hibernate** - ORM
- **MySQL 8.0** - Cơ sở dữ liệu chính
- **Maven** - Quản lý dependencies & build tool
- **Lombok, MapStruct** - Giảm boilerplate code

### Frontend
- **React 18** + **TypeScript**
- **Vite/CRA** - Build tool
- **Ant Design / MUI** - UI Component Library
- **Axios** - HTTP Client
- **React Router v6** - Routing
- **Zustand/Redux Toolkit** - State management

### DevOps & Tools
- **Docker** + **Docker Compose** - Container hóa
- **Nginx** - Reverse proxy cho production
- **Swagger/OpenAPI 3.0** - API Docs

## Cấu trúc dự án

58 lines hidden
hr-managerment-system/
├── src/ # Backend Spring Boot
│ ├── main/
│ │ ├── java/com/hr/ # Source code Java
│ │ └── resources/
│ │ ├── application.yml # Cấu hình Spring Boot
│ │ └── db/migration/ # Flyway/Liquibase scripts
│ └── test/ # Unit & Integration tests
├── public/ # Static files frontend
├── src/ # Source frontend React, nằm cùng cấp hoặc tách thư mục client/
├── .mvn/ # Maven Wrapper
├── Dockerfile # Build backend image
├── docker-compose.yml # Orchestrate BE + FE + DB
├── pom.xml # Dependencies backend
├── package.json # Dependencies frontend
└── README.md

Code
> Nếu bạn tách frontend ra thư mục `client/` hoặc `frontend/` thì sửa lại cây thư mục trên cho đúng nhé.

## Yêu cầu hệ thống
Để chạy thủ công, cần cài đặt:
1. **JDK 17+**
2. **Node.js 18+** và **npm 9+** hoặc **yarn**
3. **MySQL 8.0+** hoặc **Docker**
4. **Maven 3.8+** [không bắt buộc nếu dùng `./mvnw`]

## Cài đặt và Chạy

### Cách 1: Dùng Docker - Khuyến nghị
Nhanh nhất, không cần cài JDK, Node, MySQL.

```bash
# 1. Clone project
git clone https://github.com/doanhquang039-code/hr-managerment-system.git
cd hr-managerment-system

# 2. Khởi chạy tất cả services: backend + frontend + mysql
docker-compose up --build -d

# 3. Truy cập
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080/api
# Swagger UI: http://localhost:8080/swagger-ui.html

21 lines hidden
Dừng hệ thống: docker-compose down

Cách 2: Chạy thủ công
Bước 1: Chuẩn bị Database

SQL
CREATE DATABASE hr_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
Bước 2: Chạy Backend

Bash
# Cập nhật thông tin DB trong src/main/resources/application.yml
./mvnw clean install
./mvnw spring-boot:run
Backend chạy tại http://localhost:8080

Bước 3: Chạy Frontend

Bash
npm install
npm run dev   # hoặc npm start tùy cấu hình
Frontend chạy tại http://localhost:3000

Biến môi trường
Tạo file .env ở root hoặc cấu hình trong application.yml:

Backend application.yml

YAML
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hr_management
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
jwt:
  secret: your-256-bit-secret
  expiration: 86400000 # 1 day

6 lines hidden
Frontend .env

env
VITE_API_BASE_URL=http://localhost:8080/api
API Documentation
Sau khi chạy backend, truy cập Swagger UI để xem chi tiết tất cả endpoint:

http://localhost:8080/swagger-ui.html

Method

Endpoint

Mô tả

Phân quyền

POST

/api/auth/login

Đăng nhập, trả về JWT

Public

GET

/api/employees

Lấy danh sách nhân viên, có phân trang

HR, Admin

POST

/api/employees

Tạo nhân viên mới

HR, Admin

GET

/api/departments

Lấy cây phòng ban

All

POST

/api/attendance/check-in

Chấm công vào

Employee

Đóng góp
Mọi đóng góp đều được chào đón!

Fork repo
Tạo branch mới: git checkout -b feature/ten-tinh-nang
Commit thay đổi: git commit -m 'Add: them chuc nang X'
Push lên branch: git push origin feature/ten-tinh-nang
Tạo Pull Request
License
Dự án này được cấp phép theo MIT License [blocked]. Bạn có thể tự do sử dụng cho mục đích cá nhân và thương mại.

Liên hệ
Tác giả: Doanh - @doanhquang039-code
