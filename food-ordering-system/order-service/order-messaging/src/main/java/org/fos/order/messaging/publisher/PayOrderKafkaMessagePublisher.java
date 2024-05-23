package org.fos.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.model.avro.order.RestaurantApprovalRequestAvroModel;
import org.fos.kafka.producer.service.KafkaProducer;
import org.fos.order.app.service.config.OrderServiceConfigData;
import org.fos.order.app.service.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import org.fos.order.domain.core.event.OrderPaidEvent;
import org.fos.order.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;

    private final OrderServiceConfigData orderServiceConfigData;

    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;

    private final OrderKafkaMessagePublisherHelper orderKafkaMessagePublisherHelper;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        try {
            String orderId = domainEvent.getOrder().getId().getValue().toString();
            RestaurantApprovalRequestAvroModel req = orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestModel(domainEvent);

            CompletableFuture<SendResult<String, RestaurantApprovalRequestAvroModel>> future = kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderId, req);

            future.whenComplete((result, ex) -> orderKafkaMessagePublisherHelper.handleCallback(
                    result, orderId,
                    orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    req, "RestaurantApprovalRequestAvroModel", ex
            ));
            log.info("RestaurantApprovalRequestAvroModel sent to kafka with order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending Restaurant Approval req to Kafka with order id: {}, error: {}", domainEvent.getOrder().getId().getValue(), e);
        }
    }
}
