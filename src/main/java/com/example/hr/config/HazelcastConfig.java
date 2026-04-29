package com.example.hr.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hazelcast Configuration
 * Cấu hình Hazelcast cho distributed caching
 */
@Configuration
@ConditionalOnProperty(name = "hazelcast.enabled", havingValue = "true")
public class HazelcastConfig {

    @Value("${hazelcast.cluster-name}")
    private String clusterName;

    @Value("${hazelcast.network.port}")
    private int port;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setClusterName(clusterName);
        
        // Network configuration
        config.getNetworkConfig().setPort(port);
        config.getNetworkConfig().setPortAutoIncrement(true);
        
        // Map configurations
        MapConfig employeeMapConfig = new MapConfig();
        employeeMapConfig.setName("employees");
        employeeMapConfig.setTimeToLiveSeconds(3600); // 1 hour
        config.addMapConfig(employeeMapConfig);
        
        MapConfig attendanceMapConfig = new MapConfig();
        attendanceMapConfig.setName("attendance");
        attendanceMapConfig.setTimeToLiveSeconds(1800); // 30 minutes
        config.addMapConfig(attendanceMapConfig);
        
        return Hazelcast.newHazelcastInstance(config);
    }
}
