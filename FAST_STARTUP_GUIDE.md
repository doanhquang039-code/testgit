# ⚡ FAST STARTUP GUIDE

## 🐌 Vấn đề: Khởi động chậm (~40 giây)

### Nguyên nhân:
1. **Kafka** - Cố kết nối localhost:29092 (mất ~10s)
2. **Google Drive API** - Load credentials (mất ~30s)
3. **Hibernate** - Scan 429 files, validate schema
4. **Hazelcast** - Multicast discovery
5. **72 JPA Repositories** - Initialize tất cả

---

## ⚡ Giải pháp: Dùng DEV Profile

### Cách 1: Chạy với Maven
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Cách 2: Chạy JAR
```bash
java -jar target/hr-management-system-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### Cách 3: Set trong IDE
**IntelliJ IDEA:**
1. Run → Edit Configurations
2. Active profiles: `dev`
3. Apply → Run

**VS Code:**
1. Mở `launch.json`
2. Thêm: `"args": ["--spring.profiles.active=dev"]`

---

## 📊 So sánh thời gian khởi động

| Profile | Thời gian | Services |
|---------|-----------|----------|
| **default** | ~40s | Tất cả services |
| **dev** | ~10s | Chỉ core services |

---

## 🔧 Dev Profile tắt gì?

### ❌ Services bị tắt (không cần khi dev):
- ❌ Kafka
- ❌ Google Drive API
- ❌ Firebase
- ❌ AI/Gemini
- ❌ AWS S3
- ❌ Payment Gateways (MoMo, VNPay)
- ❌ SendGrid Email
- ❌ RabbitMQ
- ❌ Elasticsearch
- ❌ Zipkin Tracing
- ❌ Web3j Blockchain

### ✅ Services vẫn chạy:
- ✅ MySQL Database
- ✅ Spring Security
- ✅ Thymeleaf Templates
- ✅ REST APIs
- ✅ GraphQL
- ✅ Hazelcast Cache (TCP mode)
- ✅ Quartz Scheduler
- ✅ Actuator
- ✅ Swagger UI

---

## 🎯 Tối ưu thêm

### 1. Tắt Hazelcast hoàn toàn
Thêm vào `application-dev.properties`:
```properties
hazelcast.enabled=false
```

### 2. Tắt Quartz Scheduler
```properties
spring.quartz.job-store-type=none
```

### 3. Tắt GraphQL
```properties
spring.graphql.graphiql.enabled=false
```

### 4. Tắt Actuator
```properties
management.endpoints.enabled-by-default=false
```

### 5. Tắt Swagger
```properties
springdoc.swagger-ui.enabled=false
```

---

## 🚀 Khởi động siêu nhanh (~5 giây)

Tạo file `application-ultrafast.properties`:
```properties
# Inherit from dev
spring.profiles.include=dev

# Disable everything except core
hazelcast.enabled=false
spring.quartz.job-store-type=none
spring.graphql.graphiql.enabled=false
springdoc.swagger-ui.enabled=false
management.endpoints.enabled-by-default=false

# Skip Flyway
spring.flyway.enabled=false

# Minimal logging
logging.level.root=ERROR
logging.level.com.example.hr=WARN
```

Chạy:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=ultrafast
```

---

## 📝 Lưu ý

### Khi nào dùng profile nào?

| Profile | Khi nào dùng |
|---------|--------------|
| **default** | Production, test đầy đủ tính năng |
| **dev** | Development hàng ngày |
| **ultrafast** | Chỉ test UI/logic, không cần services |

### Services cần thiết cho từng tính năng:

**Chỉ cần UI/CRUD:**
→ Dùng `ultrafast`

**Cần Email/Notifications:**
→ Dùng `dev` + enable SendGrid

**Cần File Upload:**
→ Dùng `dev` + enable Google Drive hoặc Cloudinary

**Cần Payment:**
→ Dùng `default` + config MoMo/VNPay

**Cần Real-time:**
→ Dùng `default` + Kafka + WebSocket

---

## 🎉 Kết quả

Với **dev profile**, application sẽ khởi động trong **~10 giây** thay vì 40 giây!

```bash
# Trước
./mvnw spring-boot:run
# → 40 giây ⏰

# Sau
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
# → 10 giây ⚡
```

---

## 🔍 Debug startup time

Xem chi tiết thời gian khởi động:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Ddebug
```

Hoặc thêm vào `application-dev.properties`:
```properties
logging.level.org.springframework.boot.autoconfigure=DEBUG
```

---

## 💡 Tips

1. **Luôn dùng dev profile** khi develop
2. **Chỉ test production profile** trước khi deploy
3. **Tắt services không dùng** để tiết kiệm RAM
4. **Dùng Docker Compose** cho services external (Kafka, Redis, etc.)

---

**Happy Fast Coding! ⚡**
