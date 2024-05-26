package org.fos.restaurant.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.model.avro.order.RestaurantApprovalResponseAvroModel;
import org.fos.kafka.producer.service.KafkaMessagePublisherHelper;
import org.fos.kafka.producer.service.KafkaProducer;
import org.fos.restaurant.app.service.config.RestaurantServiceConfig;
import org.fos.restaurant.app.service.ports.output.publisher.OrderApprovedMessagePublisher;
import org.fos.restaurant.domain.core.event.OrderApprovedEvent;
import org.fos.restaurant.messaging.mapper.RestaurantMessagingDataMapper;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApprovedKafkaMessagePublisher implements OrderApprovedMessagePublisher {

    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfig restaurantServiceConfig;
    private final KafkaMessagePublisherHelper kafkaMessagePublisherHelper;

    @Override
    public void publish(OrderApprovedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();
        log.info("Received OrderApprovedEvent for order id: {}", orderId);
        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = restaurantMessagingDataMapper.orderApprovedEventToRestaurantApprovalResponseAvroModel(domainEvent);
            CompletableFuture<SendResult<String, RestaurantApprovalResponseAvroModel>> future = kafkaProducer.send(
                    restaurantServiceConfig.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    restaurantApprovalResponseAvroModel);

            future.whenComplete((result, ex) -> kafkaMessagePublisherHelper.handleCallback(
                    result, orderId, restaurantServiceConfig.getRestaurantApprovalResponseTopicName(),
                    restaurantApprovalResponseAvroModel, "RestaurantApprovalResponseAvroModel", ex));

            log.info("RestaurantApprovalResponseAvroModel is sent to kafka at: {}", System.nanoTime());
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalResponseAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
