# Quick Start Guide - HR Management System

## 🚀 Khởi động nhanh

### 1. Prerequisites
```bash
- Docker & Docker Compose
- Java 21
- Maven (hoặc dùng ./mvnw)
```

### 2. Start All Services
```bash
cd hr-management-system
docker-compose up -d
```

### 3. Wait for Services to be Ready
```bash
# Check status
docker-compose ps

# Wait until all services are healthy
docker-compose logs -f app
```

### 4. Access Applications

| Service | URL | Credentials |
|---------|-----|-------------|
| **HR System** | http://localhost:8080 | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **GraphiQL** | http://localhost:8080/graphiql | - |
| **Camunda** | http://localhost:8080/camunda | admin/admin |
| **Grafana** | http://localhost:3000 | admin/admin |
| **Prometheus** | http://localhost:9090 | - |
| **Kibana** | http://localhost:5601 | - |
| **RabbitMQ** | http://localhost:15672 | guest/guest |
| **Zipkin** | http://localhost:9411 | - |

---

## 📊 Monitoring

### Prometheus Metrics
```bash
# Access Prometheus
http://localhost:9090

# Query examples
http_server_requests_seconds_count
jvm_memory_used_bytes
kafka_consumer_records_consumed_total
```

### Grafana Dashboards
```bash
# Access Grafana
http://localhost:3000

# Login: admin/admin

# Add Prometheus datasource
Configuration > Data Sources > Add Prometheus
URL: http://prometheus:9090
```

### Zipkin Tracing
```bash
# Access Zipkin
http://localhost:9411

# View traces
Search by service name: hr-management-system
```

---

## 🔍 Search with Elasticsearch

### Index Employee
```bash
POST http://localhost:8080/api/search/employees/index
```

### Search Employees
```bash
GET http://localhost:8080/api/search/employees?q=john
GET http://localhost:8080/api/search/employees?department=IT
GET http://localhost:8080/api/search/employees?skill=java
```

### Kibana
```bash
# Access Kibana
http://localhost:5601

# Create index pattern: employees
# Explore data in Discover tab
```

---

## 💬 Real-time with WebSocket

### JavaScript Client
```javascript
// Connect
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to notifications
    stompClient.subscribe('/topic/notifications', function(message) {
        console.log('Notification:', JSON.parse(message.body));
    });
    
    // Subscribe to attendance updates
    stompClient.subscribe('/topic/attendance', function(message) {
        console.log('Attendance:', JSON.parse(message.body));
    });
});

// Send message
stompClient.send("/app/notification/broadcast", {}, 
    JSON.stringify({
        'title': 'Test',
        'message': 'Hello World'
    })
);
```

---

## 📨 Message Queue with RabbitMQ

### Send Email Task
```java
@Autowired
private EmailQueueProducer emailQueueProducer;

Map<String, Object> emailData = new HashMap<>();
emailData.put("to", "user@example.com");
emailData.put("subject", "Welcome");
emailData.put("body", "Welcome to HR System");

emailQueueProducer.sendEmailTask(emailData);
```

### RabbitMQ Management
```bash
# Access RabbitMQ Management UI
http://localhost:15672

# Login: guest/guest

# View queues, exchanges, messages
```

---

## 🎯 GraphQL API

### Access GraphiQL
```bash
http://localhost:8080/graphiql
```

### Example Queries
```graphql
# Get all employees
query {
  employees(page: 0, size: 10) {
    content {
      id
      fullName
      email
      department {
        name
      }
      position
    }
    totalElements
  }
}

# Get single employee
query {
  employee(id: "1") {
    id
    fullName
    email
    phone
    department {
      name
    }
  }
}

# Search employees
query {
  searchEmployees(keyword: "john") {
    id
    fullName
    email
    position
  }
}
```

### Example Mutations
```graphql
# Check in
mutation {
  checkIn(userId: "1", latitude: 10.762622, longitude: 106.660172) {
    id
    checkInTime
    status
  }
}

# Check out
mutation {
  checkOut(userId: "1") {
    id
    checkOutTime
    workingHours
  }
}
```

---

## ⏰ Scheduled Jobs

### View Quartz Jobs
```sql
-- Connect to MySQL
mysql -h localhost -P 3307 -u root -p

USE hr_management_system;

-- View all jobs
SELECT * FROM QRTZ_JOB_DETAILS;

-- View triggers
SELECT * FROM QRTZ_TRIGGERS;

-- View fired triggers
SELECT * FROM QRTZ_FIRED_TRIGGERS;
```

### Jobs Configured
1. **PayrollGenerationJob** - Runs monthly (1st day)
2. **BirthdayWishesJob** - Runs daily (9:00 AM)
3. **AttendanceReminderJob** - Runs hourly

---

## 🔄 Kafka Events

### Produce Event
```java
@Autowired
private HREventProducer eventProducer;

// Attendance event
AttendanceEvent event = new AttendanceEvent(
    1, 123, "john.doe", "John Doe",
    "CHECK_IN", LocalDateTime.now(),
    "Office", 10.762622, 106.660172,
    "Mobile App", "On time"
);

eventProducer.publishAttendanceEvent(event);
```

### Monitor Kafka
```bash
# List topics
docker exec -it hr_kafka kafka-topics --list --bootstrap-server localhost:9092

# Consume messages
docker exec -it hr_kafka kafka-console-consumer \
  --topic hr-attendance \
  --from-beginning \
  --bootstrap-server localhost:9092

# Check consumer groups
docker exec -it hr_kafka kafka-consumer-groups \
  --list \
  --bootstrap-server localhost:9092

# Check consumer lag
docker exec -it hr_kafka kafka-consumer-groups \
  --describe \
  --group hr-management-group \
  --bootstrap-server localhost:9092
```

---

## 🛡️ Resilience Patterns

### Circuit Breaker
```java
@CircuitBreaker(name = "emailService", fallbackMethod = "fallbackSendEmail")
public void sendEmail(String to, String subject, String body) {
    // Send email
}

public void fallbackSendEmail(String to, String subject, String body, Exception e) {
    log.error("Email service is down, using fallback", e);
    // Queue email for later
}
```

### Rate Limiter
```java
@RateLimiter(name = "publicApi")
public ResponseEntity<?> publicEndpoint() {
    // Handle request
}
```

### Retry
```java
@Retry(name = "emailService", fallbackMethod = "fallbackSendEmail")
public void sendEmail(String to, String subject, String body) {
    // Send email with retry
}
```

---

## 🔧 Troubleshooting

### Check Service Health
```bash
# Application health
curl http://localhost:8080/actuator/health

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus

# All actuator endpoints
curl http://localhost:8080/actuator
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
docker-compose logs -f kafka
docker-compose logs -f elasticsearch
```

### Restart Services
```bash
# Restart all
docker-compose restart

# Restart specific service
docker-compose restart app
docker-compose restart kafka
```

### Clean Up
```bash
# Stop all services
docker-compose down

# Remove volumes (WARNING: deletes all data)
docker-compose down -v

# Remove images
docker-compose down --rmi all
```

---

## 📝 Common Tasks

### 1. Add New Employee
```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "john.doe",
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

### 2. Check In
```bash
POST http://localhost:8080/api/attendance/check-in
Content-Type: application/json

{
  "userId": 1,
  "latitude": 10.762622,
  "longitude": 106.660172
}
```

### 3. Submit Leave Request
```bash
POST http://localhost:8080/api/leave-requests
Content-Type: application/json

{
  "userId": 1,
  "leaveType": "ANNUAL",
  "startDate": "2026-05-01",
  "endDate": "2026-05-03",
  "reason": "Vacation"
}
```

### 4. Search Employees
```bash
GET http://localhost:8080/api/search/employees?q=john
```

### 5. View Metrics
```bash
GET http://localhost:8080/actuator/metrics
GET http://localhost:8080/actuator/metrics/jvm.memory.used
GET http://localhost:8080/actuator/metrics/http.server.requests
```

---

## 🎓 Next Steps

1. **Explore Swagger UI** - Test all APIs
2. **Create Grafana Dashboards** - Visualize metrics
3. **Setup Camunda Workflows** - Automate processes
4. **Implement GraphQL Resolvers** - Complete GraphQL API
5. **Add Elasticsearch Indexing** - Enable full-text search
6. **Configure Alerts** - Setup Prometheus alerts

---

## 📚 Documentation

- [KAFKA_INTEGRATION_GUIDE.md](KAFKA_INTEGRATION_GUIDE.md) - Kafka setup
- [TECHNOLOGY_STACK_COMPLETE.md](TECHNOLOGY_STACK_COMPLETE.md) - Full tech stack
- [README.md](README.md) - Project overview

---

## 🆘 Support

### Issues?
1. Check logs: `docker-compose logs -f`
2. Check health: `curl http://localhost:8080/actuator/health`
3. Restart services: `docker-compose restart`

### Performance Issues?
1. Check Prometheus metrics
2. View Zipkin traces
3. Check Elasticsearch indices
4. Monitor Kafka consumer lag

---

**Happy Coding! 🚀**
