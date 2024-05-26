package org.fos.restaurant.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"org.fos.restaurant.data.access", "org.fos.common.domain"})
@EntityScan(basePackages = {"org.fos.restaurant.data.access", "org.fos.common.domain"})
@SpringBootApplication(scanBasePackages = {"org.fos.restaurant","org.fos.kafka"})
public class RestaurantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}