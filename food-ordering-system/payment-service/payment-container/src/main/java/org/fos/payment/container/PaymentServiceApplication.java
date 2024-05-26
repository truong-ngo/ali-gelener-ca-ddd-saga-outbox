package org.fos.payment.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.fos.payment.data.access")
@EntityScan(basePackages = "org.fos.payment.data.access")
@SpringBootApplication(scanBasePackages = {"org.fos.payment","org.fos.kafka"})
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}