package org.fos.payment.app.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payment-service")
public class PaymentConfigData {
    private String paymentRequestTopicName;
    private String paymentResponseTopicName;
}
