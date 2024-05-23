package org.fos.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.model.avro.order.PaymentRequestAvroModel;
import org.fos.kafka.producer.service.KafkaProducer;
import org.fos.order.app.service.config.OrderServiceConfigData;
import org.fos.order.app.service.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.fos.order.domain.core.event.OrderCreatedEvent;
import org.fos.order.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;

    private final OrderServiceConfigData orderServiceConfigData;

    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    private final OrderKafkaMessagePublisherHelper orderKafkaMessagePublisherHelper;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        try {
            String orderId = domainEvent.getOrder().getId().getValue().toString();
            log.info("Received OrderCreatedEvent for order id: {}", orderId);
            PaymentRequestAvroModel req = orderMessagingDataMapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);
            CompletableFuture<SendResult<String, PaymentRequestAvroModel>> future = kafkaProducer.send(orderServiceConfigData.getPaymentRequestTopicName(), orderId, req);
            future.whenComplete((result, ex) -> orderKafkaMessagePublisherHelper.handleCallback(
                    result, orderId,
                    orderServiceConfigData.getPaymentResponseTopicName(),
                    req, "PaymentRequestAvroModel", ex));
            log.info("Payment request sent to Kafka with order id: {}", req.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending Payment request to Kafka with order id: {}, error: {}", domainEvent.getOrder().getId().getValue(), e);
        }
    }


}
