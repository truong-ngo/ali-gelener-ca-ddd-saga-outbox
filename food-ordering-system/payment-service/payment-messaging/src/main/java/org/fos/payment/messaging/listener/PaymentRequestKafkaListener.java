package org.fos.payment.messaging.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.kafka.consumer.KafkaConsumer;
import org.fos.kafka.model.avro.order.PaymentOrderStatus;
import org.fos.kafka.model.avro.order.PaymentRequestAvroModel;
import org.fos.payment.app.service.ports.input.message.listener.PaymentRequestMessageListener;
import org.fos.payment.messaging.mapper.PaymentMessagingDataMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
                   topics = "${payment-service.payment-request-topic-name}")
    public void receive(
            @Payload List<PaymentRequestAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment requests received with keys {}, partition {}, and offset {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString()
        );

        messages.forEach(m -> {
            if (PaymentOrderStatus.PENDING == m.getPaymentOrderStatus()) {
                log.info("Processing payment for order id: {}", m.getOrderId());
                paymentRequestMessageListener.completePayment(paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(m));
            } else if (PaymentOrderStatus.CANCELLED == m.getPaymentOrderStatus()) {
                log.info("Cancelling payment for order id: {}", m.getOrderId());
                paymentRequestMessageListener.cancelPayment(paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(m));
            }
        });
    }
}
