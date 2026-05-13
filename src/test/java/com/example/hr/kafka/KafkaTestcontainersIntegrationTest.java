package com.example.hr.kafka;

import com.example.hr.kafka.events.NotificationEvent;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
class KafkaTestcontainersIntegrationTest {

    private static final String TOPIC = "hr-notifications-it";

    @Container
    static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );

    @Test
    void publishesAndConsumesJsonNotificationEvent() throws Exception {
        createTopic(TOPIC);

        KafkaTemplate<String, Object> template = kafkaTemplate();
        NotificationEvent event = new NotificationEvent(
                "IN_APP",
                List.of(1),
                "Kafka integration test",
                "Kafka Testcontainers message",
                "MEDIUM",
                "TEST",
                99,
                "TEST_ENTITY",
                LocalDateTime.now()
        );

        template.send(TOPIC, "notification-1", event).get();

        try (KafkaConsumer<String, NotificationEvent> consumer = notificationConsumer()) {
            consumer.subscribe(List.of(TOPIC));

            ConsumerRecord<String, NotificationEvent> record = consumer.poll(Duration.ofSeconds(10))
                    .records(TOPIC)
                    .iterator()
                    .next();

            assertThat(record.key()).isEqualTo("notification-1");
            assertThat(record.value().getSubject()).isEqualTo("Kafka integration test");
            assertThat(record.value().getRecipientUserIds()).containsExactly(1);
        }
    }

    private static void createTopic(String topic) throws Exception {
        try (AdminClient adminClient = AdminClient.create(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers()
        ))) {
            adminClient.createTopics(List.of(new NewTopic(topic, 1, (short) 1))).all().get();
        }
    }

    private static KafkaTemplate<String, Object> kafkaTemplate() {
        Map<String, Object> props = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    private static KafkaConsumer<String, NotificationEvent> notificationConsumer() {
        JsonDeserializer<NotificationEvent> valueDeserializer = new JsonDeserializer<>(NotificationEvent.class);
        valueDeserializer.addTrustedPackages("com.example.hr.kafka.events");
        valueDeserializer.setUseTypeHeaders(false);

        return new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "hr-kafka-it-" + UUID.randomUUID(),
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
                ),
                new StringDeserializer(),
                valueDeserializer
        );
    }
}
