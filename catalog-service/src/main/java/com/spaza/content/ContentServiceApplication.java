package com.spaza.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.spaza")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"com.spaza.category.model", "com.spaza.product.model", "com.spaza.product.entity", "com.spaza.manufacturer.entity", "com.spaza.content.model"})
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = {"com.spaza.category.repository", "com.spaza.product.repository", "com.spaza.manufacturer.repository", "com.spaza.content.repository"})
public class ContentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentServiceApplication.class, args);
    }
}