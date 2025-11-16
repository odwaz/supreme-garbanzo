package com.spaza.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.spaza.customer", "com.spaza.cart"})
@EnableJpaRepositories(basePackages = {"com.spaza.customer.repository", "com.spaza.cart.repository"})
@EntityScan(basePackages = {"com.spaza.customer.model", "com.spaza.cart.model"})
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}