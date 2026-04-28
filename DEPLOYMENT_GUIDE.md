# HR Management System - Deployment Guide

## 📋 Yêu cầu hệ thống

### Backend:
- **Java**: 17 hoặc cao hơn
- **Maven**: 3.8+
- **Database**: PostgreSQL 14+ hoặc MySQL 8+
- **Memory**: Tối thiểu 2GB RAM

### Frontend:
- **Browser**: Chrome, Firefox, Safari, Edge (phiên bản mới nhất)
- **JavaScript**: Enabled

---

## 🚀 Hướng dẫn cài đặt

### 1. Clone Repository

```bash
git clone <repository-url>
cd hr-management-system
```

### 2. Cấu hình Database

#### PostgreSQL:
```sql
CREATE DATABASE hr_management;
CREATE USER hr_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE hr_management TO hr_user;
```

#### MySQL:
```sql
CREATE DATABASE hr_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'hr_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON hr_management.* TO 'hr_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Cấu hình Application

Tạo file `.env` hoặc cập nhật `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/hr_management
spring.datasource.username=hr_user
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Security
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# Email Configuration (Optional)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Build Application

```bash
# Windows
mvnw.cmd clean package -DskipTests

# Linux/Mac
./mvnw clean package -DskipTests
```

### 5. Run Application

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run

# Hoặc chạy JAR file
java -jar target/hr-management-system-0.0.1-SNAPSHOT.jar
```

### 6. Truy cập Application

Mở browser và truy cập:
```
http://localhost:8080
```

---

## 🐳 Docker Deployment

### 1. Build Docker Image

```bash
docker build -t hr-management-system .
```

### 2. Run với Docker Compose

```bash
docker-compose up -d
```

File `docker-compose.yml`:
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: hr_management
      POSTGRES_USER: hr_user
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/hr_management
      SPRING_DATASOURCE_USERNAME: hr_user
      SPRING_DATASOURCE_PASSWORD: your_password
    depends_on:
      - postgres

volumes:
  postgres_data:
```

---

## 🔐 Tài khoản mặc định

### Admin:
- **Username**: admin
- **Password**: admin123

### HR Manager:
- **Username**: hr
- **Password**: hr123

### Employee:
- **Username**: employee
- **Password**: employee123

**⚠️ Lưu ý**: Đổi mật khẩu ngay sau khi đăng nhập lần đầu!

---

## 📡 API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/logout` - Đăng xuất

### LMS
- `GET /api/lms/courses` - Danh sách khóa học
- `POST /api/lms/enroll/{courseId}` - Đăng ký khóa học
- `GET /api/lms/my-courses` - Khóa học của tôi

### Attendance
- `POST /api/attendance/geofencing-checkin` - Chấm công GPS
- `POST /api/attendance/face-recognition/register` - Đăng ký khuôn mặt
- `GET /api/attendance/my-schedule` - Lịch làm việc

### Assets
- `GET /api/assets/my-assets` - Tài sản của tôi
- `POST /api/assets/create` - Tạo tài sản (Admin)
- `POST /api/assets/assign` - Giao tài sản (Admin)

### Engagement
- `GET /api/engagement/posts` - Danh sách bài viết
- `POST /api/engagement/post/create` - Tạo bài viết
- `POST /api/engagement/recognition/give` - Tặng vinh danh

### Onboarding
- `GET /api/onboarding/my-checklist` - Checklist của tôi
- `POST /api/onboarding/checklist/{itemId}/complete` - Hoàn thành task
- `POST /api/onboarding/exit-interview/submit` - Nộp phỏng vấn nghỉ việc

---

## 🔧 Troubleshooting

### Lỗi kết nối Database
```
Error: Could not connect to database
```
**Giải pháp**:
- Kiểm tra database đã chạy chưa
- Kiểm tra username/password
- Kiểm tra firewall

### Lỗi Port đã được sử dụng
```
Error: Port 8080 is already in use
```
**Giải pháp**:
- Đổi port trong `application.properties`: `server.port=8081`
- Hoặc kill process đang dùng port 8080

### Lỗi Out of Memory
```
Error: Java heap space
```
**Giải pháp**:
```bash
java -Xmx2g -jar target/hr-management-system-0.0.1-SNAPSHOT.jar
```

---

## 📊 Monitoring

### Health Check
```
GET http://localhost:8080/actuator/health
```

### Metrics
```
GET http://localhost:8080/actuator/metrics
```

---

## 🔄 Update & Maintenance

### Backup Database
```bash
# PostgreSQL
pg_dump -U hr_user hr_management > backup.sql

# MySQL
mysqldump -u hr_user -p hr_management > backup.sql
```

### Restore Database
```bash
# PostgreSQL
psql -U hr_user hr_management < backup.sql

# MySQL
mysql -u hr_user -p hr_management < backup.sql
```

### Update Application
```bash
git pull
mvnw clean package -DskipTests
# Restart application
```

---

## 🌐 Production Deployment

### Nginx Configuration

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### SSL Certificate (Let's Encrypt)
```bash
sudo certbot --nginx -d your-domain.com
```

### Systemd Service

Tạo file `/etc/systemd/system/hr-management.service`:

```ini
[Unit]
Description=HR Management System
After=syslog.target network.target

[Service]
User=hr-app
ExecStart=/usr/bin/java -jar /opt/hr-management/hr-management-system.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable và start service:
```bash
sudo systemctl enable hr-management
sudo systemctl start hr-management
sudo systemctl status hr-management
```

---

## 📞 Support

Nếu gặp vấn đề, vui lòng:
1. Kiểm tra logs: `logs/spring.log`
2. Xem documentation: `README.md`
3. Liên hệ support team

---

**Version**: 2.0.0  
**Last Updated**: 28/04/2026
