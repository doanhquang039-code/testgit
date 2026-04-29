# Troubleshooting Guide

## ❌ Lỗi thường gặp và cách fix

### 1. Elasticsearch Connection Refused

**Lỗi:**
```
Caused by: java.net.ConnectException: Connection refused: getsockopt
at org.elasticsearch.client.RestClient.extractAndWrapCause
```

**Nguyên nhân:** Elasticsearch chưa chạy

**Cách fix:**

#### Option 1: Disable Elasticsearch (Quick fix)
```properties
# src/main/resources/application.properties
spring.data.elasticsearch.repositories.enabled=false
```

Và comment `@Configuration` trong `ElasticsearchConfig.java`:
```java
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.example.hr.elasticsearch.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {
```

#### Option 2: Start Elasticsearch
```bash
docker-compose up -d elasticsearch kibana
```

Sau đó enable lại:
```properties
spring.data.elasticsearch.repositories.enabled=true
```

---

### 2. Port 8080 Already in Use

**Lỗi:**
```
Port 8080 was already in use
```

**Cách fix:**

#### Windows:
```bash
# Tìm process đang dùng port 8080
netstat -ano | findstr :8080

# Kill process (thay PID bằng số từ lệnh trên)
taskkill /PID <PID> /F
```

#### Linux/Mac:
```bash
# Tìm và kill process
lsof -ti:8080 | xargs kill -9
```

---

### 3. MySQL Connection Error

**Lỗi:**
```
Communications link failure
```

**Cách fix:**

1. Check MySQL đang chạy:
```bash
docker ps | grep mysql
```

2. Start MySQL nếu chưa chạy:
```bash
docker-compose up -d mysql
```

3. Check connection string trong `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hr_management_system
spring.datasource.username=root
spring.datasource.password=123456
```

---

### 4. Redis Connection Error

**Lỗi:**
```
Unable to connect to Redis
```

**Cách fix:**

1. Start Redis:
```bash
docker-compose up -d redis
```

2. Hoặc disable Redis cache:
```properties
spring.cache.type=none
```

---

### 5. Kafka Connection Error

**Lỗi:**
```
Connection to node -1 could not be established
```

**Cách fix:**

1. Start Kafka và Zookeeper:
```bash
docker-compose up -d zookeeper kafka
```

2. Wait for Kafka to be ready (30-60 seconds)

3. Check Kafka logs:
```bash
docker logs hr_kafka
```

---

### 6. RabbitMQ Connection Error

**Lỗi:**
```
Connection refused to RabbitMQ
```

**Cách fix:**

1. Start RabbitMQ:
```bash
docker-compose up -d rabbitmq
```

2. Access management UI:
```
http://localhost:15672
Username: guest
Password: guest
```

---

### 7. Hazelcast Warning

**Warning:**
```
Hazelcast is starting in a Java modular environment
```

**Cách fix:** Thêm JVM arguments (optional):
```bash
--add-modules java.se 
--add-exports java.base/jdk.internal.ref=ALL-UNNAMED 
--add-opens java.base/java.lang=ALL-UNNAMED
```

Hoặc ignore warning này (không ảnh hưởng chức năng).

---

### 8. Out of Memory Error

**Lỗi:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Cách fix:**

Tăng heap size:
```bash
# Linux/Mac
export MAVEN_OPTS="-Xmx2048m"

# Windows
set MAVEN_OPTS=-Xmx2048m

# Hoặc trong pom.xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <jvmArguments>-Xmx2048m</jvmArguments>
    </configuration>
</plugin>
```

---

### 9. Flyway Migration Error

**Lỗi:**
```
Flyway migration failed
```

**Cách fix:**

1. Repair Flyway:
```bash
./mvnw flyway:repair
```

2. Hoặc clean database:
```sql
DROP DATABASE hr_management_system;
CREATE DATABASE hr_management_system;
```

3. Restart application

---

### 10. Camunda Database Error

**Lỗi:**
```
Camunda tables not found
```

**Cách fix:**

Camunda sẽ tự tạo tables. Nếu lỗi, check:
```properties
camunda.bpm.database.schema-update=true
```

---

## 🚀 Quick Start (Minimal Setup)

Nếu muốn chạy nhanh mà không cần tất cả services:

### 1. Chỉ cần MySQL
```bash
docker-compose up -d mysql
```

### 2. Disable các service khác trong application.properties:
```properties
# Disable Elasticsearch
spring.data.elasticsearch.repositories.enabled=false

# Disable Hazelcast
hazelcast.enabled=false

# Disable Redis (optional)
spring.cache.type=none
```

### 3. Comment các @Configuration không cần:
- `ElasticsearchConfig.java` - Comment `@Configuration`
- `HazelcastConfig.java` - Comment `@Configuration`
- `RabbitMQConfig.java` - Comment `@Configuration`

### 4. Run application:
```bash
./mvnw spring-boot:run
```

---

## 🐳 Docker Services Management

### Start all services:
```bash
docker-compose up -d
```

### Start specific services:
```bash
docker-compose up -d mysql redis
```

### Stop all services:
```bash
docker-compose down
```

### View logs:
```bash
docker-compose logs -f app
docker-compose logs -f mysql
docker-compose logs -f kafka
```

### Restart service:
```bash
docker-compose restart app
```

### Remove all (including volumes):
```bash
docker-compose down -v
```

---

## 📝 Health Checks

### Application Health:
```bash
curl http://localhost:8080/actuator/health
```

### MySQL:
```bash
docker exec -it hr_mysql mysql -uroot -p123456 -e "SELECT 1"
```

### Redis:
```bash
docker exec -it hr_redis redis-cli ping
```

### Elasticsearch:
```bash
curl http://localhost:9200/_cluster/health
```

### Kafka:
```bash
docker exec -it hr_kafka kafka-topics --list --bootstrap-server localhost:9092
```

### RabbitMQ:
```bash
curl -u guest:guest http://localhost:15672/api/overview
```

---

## 🔍 Debug Mode

Enable debug logging:
```properties
logging.level.root=DEBUG
logging.level.com.example.hr=DEBUG
logging.level.org.springframework=DEBUG
```

---

## 💡 Tips

1. **Start services theo thứ tự:**
   - MySQL first
   - Redis, Zookeeper
   - Kafka, RabbitMQ, Elasticsearch
   - Application last

2. **Check logs khi có lỗi:**
   ```bash
   docker-compose logs -f
   ```

3. **Clean build khi cần:**
   ```bash
   ./mvnw clean install -DskipTests
   ```

4. **Restart Docker khi cần:**
   ```bash
   docker-compose restart
   ```

5. **Monitor resources:**
   ```bash
   docker stats
   ```

---

## 📞 Support

Nếu vẫn gặp lỗi:
1. Check logs: `docker-compose logs -f`
2. Check health: `curl http://localhost:8080/actuator/health`
3. Check ports: `netstat -ano | findstr :8080`
4. Restart services: `docker-compose restart`
5. Clean rebuild: `./mvnw clean install`
