package org.fos.order.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"org.fos.order.data.access", "org.fos.common.domain"})
@EntityScan(basePackages = {"org.fos.order.data.access", "org.fos.common.domain"})
@SpringBootApplication(scanBasePackages = {"org.fos.order","org.fos.kafka"})
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}