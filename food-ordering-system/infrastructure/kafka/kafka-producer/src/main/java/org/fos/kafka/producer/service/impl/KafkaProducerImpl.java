package org.fos.kafka.producer.service.impl;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.fos.kafka.producer.exception.KafkaProducerException;
import org.fos.kafka.producer.service.KafkaProducer;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public CompletableFuture<SendResult<K, V>> send(String topicName, K key, V message) {
        log.info("Sending message {} to topic {}", message, topicName);
        try {
            return kafkaTemplate.send(topicName, message);
        } catch (KafkaException exception) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message, exception.getMessage());
            throw new KafkaProducerException("Error on kafka producer with key: " + key + " and message: " + message);
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
