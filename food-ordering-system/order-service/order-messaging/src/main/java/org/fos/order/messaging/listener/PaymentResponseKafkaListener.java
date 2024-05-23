package org.fos.order.messaging.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.consumer.KafkaConsumer;
import org.fos.kafka.model.avro.order.PaymentResponseAvroModel;
import org.fos.kafka.model.avro.order.PaymentStatus;
import org.fos.order.app.service.ports.input.message.listener.payment.PaymentResponseMessageListener;
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
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;

    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(
            @Payload List<PaymentResponseAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET)  List<Long> offsets) {
        log.info("{} number of payments responses receive with keys: {}, partition: {} and offset: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(p -> {
            if (p.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
                log.info("Processing successful payment for order id: {}", p.getOrderId());
                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(p));
            } else if (p.getPaymentStatus().equals(PaymentStatus.CANCELLED) || p.getPaymentStatus().equals(PaymentStatus.FAILED)) {
                log.info("Processing unsuccessful payment for order id: {}", p.getOrderId());
                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(p));
            }
        });
    }
}
