package com.example.hr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch Configuration
 * Cấu hình Elasticsearch cho full-text search
 * 
 * NOTE: Disabled by default. Enable when Elasticsearch is running.
 * Set spring.data.elasticsearch.repositories.enabled=true in application.properties
 */
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.example.hr.elasticsearch.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUri;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchUri.replace("http://", ""))
                .build();
    }
}
