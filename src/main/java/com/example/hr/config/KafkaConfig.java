package com.example.hr.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka Configuration
 * Tạo các topic cho HR Management System
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.topics.attendance}")
    private String attendanceTopic;

    @Value("${kafka.topics.leave-requests}")
    private String leaveRequestsTopic;

    @Value("${kafka.topics.payroll}")
    private String payrollTopic;

    @Value("${kafka.topics.notifications}")
    private String notificationsTopic;

    @Value("${kafka.topics.performance-reviews}")
    private String performanceReviewsTopic;

    @Value("${kafka.topics.recruitment}")
    private String recruitmentTopic;

    @Value("${kafka.topics.training}")
    private String trainingTopic;

    @Value("${kafka.topics.employee-lifecycle}")
    private String employeeLifecycleTopic;

    @Bean
    public NewTopic attendanceTopic() {
        return TopicBuilder.name(attendanceTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic leaveRequestsTopic() {
        return TopicBuilder.name(leaveRequestsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic payrollTopic() {
        return TopicBuilder.name(payrollTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationsTopic() {
        return TopicBuilder.name(notificationsTopic)
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic performanceReviewsTopic() {
        return TopicBuilder.name(performanceReviewsTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic recruitmentTopic() {
        return TopicBuilder.name(recruitmentTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic trainingTopic() {
        return TopicBuilder.name(trainingTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic employeeLifecycleTopic() {
        return TopicBuilder.name(employeeLifecycleTopic)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
