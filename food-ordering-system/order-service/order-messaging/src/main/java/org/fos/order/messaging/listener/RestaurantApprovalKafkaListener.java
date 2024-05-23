package org.fos.order.messaging.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.consumer.KafkaConsumer;
import org.fos.kafka.model.avro.order.OrderApprovalStatus;
import org.fos.kafka.model.avro.order.RestaurantApprovalResponseAvroModel;
import org.fos.order.app.service.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import org.fos.order.messaging.mapper.OrderMessagingDataMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;

    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(
            @Payload List<RestaurantApprovalResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant approval responses receive with keys: {}, partition: {} and offset: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(r -> {
            if (r.getOrderApprovalStatus().equals(OrderApprovalStatus.APPROVED)) {
                log.info("Processing approved for order id: {}", r.getOrderId());
                restaurantApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper.restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(r));
            } else if (r.getOrderApprovalStatus().equals(OrderApprovalStatus.REJECTED)) {
                log.info("Processing rejected for order id: {}, with failure messages: {}", r.getOrderId(), String.join(",", r.getFailureMessages()));
                restaurantApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper.restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(r));
            }
        });
    }
}
