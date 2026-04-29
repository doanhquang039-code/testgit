# Kafka Integration Guide - HR Management System

## Tổng quan

Hệ thống HR Management System đã được tích hợp Apache Kafka để xử lý các sự kiện bất đồng bộ và xây dựng kiến trúc event-driven.

## Cấu trúc Kafka

### Topics

Hệ thống sử dụng 8 topics chính:

1. **hr-attendance** - Sự kiện chấm công
2. **hr-leave-requests** - Sự kiện đơn xin nghỉ phép
3. **hr-payroll** - Sự kiện bảng lương
4. **hr-notifications** - Sự kiện thông báo
5. **hr-performance-reviews** - Sự kiện đánh giá hiệu suất
6. **hr-recruitment** - Sự kiện tuyển dụng
7. **hr-training** - Sự kiện đào tạo
8. **hr-employee-lifecycle** - Sự kiện vòng đời nhân viên

### Event Types

#### 1. Attendance Events
- `CHECK_IN` - Nhân viên check-in
- `CHECK_OUT` - Nhân viên check-out
- `LATE` - Đi muộn
- `EARLY_LEAVE` - Về sớm
- `ABSENT` - Vắng mặt

#### 2. Leave Request Events
- `SUBMITTED` - Gửi đơn xin nghỉ
- `APPROVED` - Đơn được duyệt
- `REJECTED` - Đơn bị từ chối
- `CANCELLED` - Hủy đơn

#### 3. Payroll Events
- `GENERATED` - Tạo bảng lương
- `APPROVED` - Duyệt bảng lương
- `PAID` - Đã thanh toán
- `REJECTED` - Từ chối bảng lương

#### 4. Notification Events
- `EMAIL` - Gửi email
- `SMS` - Gửi SMS
- `PUSH` - Push notification
- `IN_APP` - Thông báo trong app

#### 5. Performance Review Events
- `CREATED` - Tạo đánh giá
- `SUBMITTED` - Gửi đánh giá
- `APPROVED` - Duyệt đánh giá
- `COMPLETED` - Hoàn thành đánh giá

#### 6. Recruitment Events
- `APPLICATION_RECEIVED` - Nhận hồ sơ ứng tuyển
- `SCREENING` - Sàng lọc hồ sơ
- `INTERVIEW_SCHEDULED` - Lên lịch phỏng vấn
- `OFFER_SENT` - Gửi offer
- `HIRED` - Tuyển dụng thành công
- `REJECTED` - Từ chối ứng viên

#### 7. Training Events
- `ENROLLED` - Đăng ký khóa học
- `STARTED` - Bắt đầu học
- `COMPLETED` - Hoàn thành khóa học
- `CANCELLED` - Hủy khóa học
- `CERTIFICATE_ISSUED` - Cấp chứng chỉ

#### 8. Employee Lifecycle Events
- `ONBOARDED` - Nhân viên mới onboard
- `PROMOTED` - Thăng chức
- `TRANSFERRED` - Chuyển phòng ban
- `RESIGNED` - Nghỉ việc
- `TERMINATED` - Chấm dứt hợp đồng

## Cấu hình

### Application Properties

```properties
# Kafka Bootstrap Servers
spring.kafka.bootstrap-servers=localhost:29092

# Consumer Configuration
spring.kafka.consumer.group-id=hr-management-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

# Producer Configuration
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
```

### Docker Compose

Kafka và Zookeeper đã được thêm vào `docker-compose.yml`:

```yaml
zookeeper:
  image: confluentinc/cp-zookeeper:7.5.0
  ports:
    - "2181:2181"

kafka:
  image: confluentinc/cp-kafka:7.5.0
  ports:
    - "9092:9092"
    - "29092:29092"
  depends_on:
    - zookeeper
```

## Sử dụng

### 1. Publish Event

```java
@Autowired
private HREventProducer eventProducer;

// Publish attendance event
AttendanceEvent event = new AttendanceEvent(
    attendanceId,
    userId,
    username,
    fullName,
    "CHECK_IN",
    LocalDateTime.now(),
    "Office",
    latitude,
    longitude,
    deviceInfo,
    notes
);

eventProducer.publishAttendanceEvent(event);
```

### 2. Consume Event

Consumers tự động lắng nghe và xử lý events:

```java
@Service
@Slf4j
public class AttendanceEventConsumer {
    
    @KafkaListener(topics = "${kafka.topics.attendance}", 
                   groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAttendanceEvent(AttendanceEvent event) {
        log.info("Received attendance event: {}", event);
        // Process event
    }
}
```

## Workflow Examples

### 1. Leave Request Workflow

```
Employee submits leave request
    ↓
LeaveRequestEvent (SUBMITTED) → Kafka
    ↓
Consumer processes event
    ↓
NotificationEvent → Manager
    ↓
Manager approves/rejects
    ↓
LeaveRequestEvent (APPROVED/REJECTED) → Kafka
    ↓
Consumer processes event
    ↓
NotificationEvent → Employee
    ↓
Update leave balance
```

### 2. Payroll Workflow

```
Generate payroll
    ↓
PayrollEvent (GENERATED) → Kafka
    ↓
HR reviews and approves
    ↓
PayrollEvent (APPROVED) → Kafka
    ↓
Process payment
    ↓
PayrollEvent (PAID) → Kafka
    ↓
NotificationEvent → Employee
```

### 3. Recruitment Workflow

```
Candidate applies
    ↓
RecruitmentEvent (APPLICATION_RECEIVED) → Kafka
    ↓
Send confirmation email
    ↓
HR screens application
    ↓
RecruitmentEvent (SCREENING) → Kafka
    ↓
Schedule interview
    ↓
RecruitmentEvent (INTERVIEW_SCHEDULED) → Kafka
    ↓
Send calendar invite
    ↓
Interview completed
    ↓
RecruitmentEvent (OFFER_SENT/REJECTED) → Kafka
```

## Lợi ích

### 1. Decoupling
- Các module không phụ thuộc trực tiếp vào nhau
- Dễ dàng thêm/xóa consumers mà không ảnh hưởng producers

### 2. Scalability
- Có thể scale consumers độc lập
- Xử lý hàng nghìn events/giây

### 3. Reliability
- Events được lưu trữ trong Kafka
- Có thể replay events khi cần
- Retry mechanism tự động

### 4. Async Processing
- Không block main thread
- Cải thiện response time
- Xử lý background tasks hiệu quả

### 5. Event Sourcing
- Lưu trữ toàn bộ lịch sử events
- Audit trail đầy đủ
- Có thể rebuild state từ events

## Monitoring

### Kafka Topics

```bash
# List topics
docker exec -it hr_kafka kafka-topics --list --bootstrap-server localhost:9092

# Describe topic
docker exec -it hr_kafka kafka-topics --describe --topic hr-attendance --bootstrap-server localhost:9092

# Check consumer groups
docker exec -it hr_kafka kafka-consumer-groups --list --bootstrap-server localhost:9092

# Check consumer lag
docker exec -it hr_kafka kafka-consumer-groups --describe --group hr-management-group --bootstrap-server localhost:9092
```

### Application Logs

Consumers log mọi events được xử lý:

```
INFO  - Received attendance event: userId=123, eventType=CHECK_IN
INFO  - Processing check-in: user=John Doe, location=Office
INFO  - Attendance event published: userId=123, eventType=CHECK_IN
```

## Troubleshooting

### 1. Kafka không kết nối được

```bash
# Check Kafka status
docker ps | grep kafka

# Check Kafka logs
docker logs hr_kafka

# Restart Kafka
docker-compose restart kafka
```

### 2. Consumer không nhận events

- Kiểm tra consumer group ID
- Kiểm tra topic name
- Kiểm tra deserializer configuration
- Check consumer logs

### 3. Producer không gửi được events

- Kiểm tra Kafka connection
- Kiểm tra serializer configuration
- Check producer logs
- Verify topic exists

## Best Practices

### 1. Event Design
- Keep events immutable
- Include timestamp
- Add correlation ID
- Use meaningful event types

### 2. Error Handling
- Implement retry logic
- Use dead letter queue
- Log all errors
- Monitor consumer lag

### 3. Performance
- Use appropriate partition count
- Batch messages when possible
- Configure proper retention
- Monitor disk usage

### 4. Security
- Use SSL/TLS for production
- Implement authentication
- Encrypt sensitive data
- Use ACLs for topics

## Future Enhancements

1. **Kafka Streams** - Real-time data processing
2. **Schema Registry** - Event schema management
3. **Kafka Connect** - Integration với external systems
4. **KSQL** - SQL queries trên Kafka streams
5. **Monitoring Dashboard** - Grafana + Prometheus
6. **Event Replay** - Replay events for debugging
7. **Dead Letter Queue** - Handle failed events
8. **Event Versioning** - Support multiple event versions

## Tài liệu tham khảo

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Documentation](https://spring.io/projects/spring-kafka)
- [Confluent Platform](https://docs.confluent.io/)
- [Event-Driven Architecture](https://martinfowler.com/articles/201701-event-driven.html)
