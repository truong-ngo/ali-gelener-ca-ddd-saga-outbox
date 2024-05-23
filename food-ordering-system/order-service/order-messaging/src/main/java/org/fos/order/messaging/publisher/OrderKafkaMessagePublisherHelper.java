package org.fos.order.messaging.publisher;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaMessagePublisherHelper {

    public <T> void handleCallback(SendResult<String, T> result, String orderId, String responseTopicName, T req, String reqModelName, Throwable ex) {
        if (ex == null) {
            handleSuccess(result, orderId);
        } else {
            handleError(responseTopicName, req, reqModelName, ex);
        }
    }

    public <T> void handleSuccess(SendResult<String, T> result, String orderId) {
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("Received successful response from Kafka for order id: {} " +
                        " Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
                orderId,
                metadata.topic(),
                metadata.partition(),
                metadata.offset(),
                metadata.timestamp());
    }

    public <T> void handleError(String responseTopicName, T req, String reqModelName, Throwable ex) {
        log.error("Error while sending " + reqModelName + " message {} to topic {}", req.toString(), responseTopicName, ex);
    }
}
