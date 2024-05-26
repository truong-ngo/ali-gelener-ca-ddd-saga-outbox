package org.fos.payment.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.model.avro.order.PaymentResponseAvroModel;
import org.fos.kafka.producer.service.KafkaMessagePublisherHelper;
import org.fos.kafka.producer.service.KafkaProducer;
import org.fos.payment.app.service.config.PaymentConfigData;
import org.fos.payment.app.service.ports.output.message.publisher.PaymentCompleteMessagePublisher;
import org.fos.payment.domain.core.event.PaymentCompletedEvent;
import org.fos.payment.messaging.mapper.PaymentMessagingDataMapper;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompleteKafkaMessagePublisher implements PaymentCompleteMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    private final PaymentConfigData paymentConfigData;

    private final KafkaMessagePublisherHelper kafkaMessagePublisherHelper;

    @Override
    public void publish(PaymentCompletedEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

        log.info("Receive PaymentCompleteEvent for order id: {}", orderId);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper.paymentCompleteEventToPaymentResponseAvroModel(domainEvent);

            CompletableFuture<SendResult<String, PaymentResponseAvroModel>> future = kafkaProducer.send(
                    paymentConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel
            );

            future.whenComplete((result, ex) -> kafkaMessagePublisherHelper.handleCallback(
                    result, orderId, paymentConfigData.getPaymentResponseTopicName(),
                    paymentResponseAvroModel, "PaymentResponseAvroModel", ex
            ));

            log.info("PaymentResponseAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentResponseAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
