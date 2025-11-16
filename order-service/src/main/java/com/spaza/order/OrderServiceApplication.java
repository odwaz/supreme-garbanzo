package com.spaza.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.spaza")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"com.spaza.tax.entity", "com.spaza.order.entity", "com.spaza.payment.model", "com.spaza.shipping.model"})
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = {"com.spaza.tax.repository", "com.spaza.order.repository", "com.spaza.payment.repository", "com.spaza.shipping.repository"})
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}