package org.fos.payment.messaging.mapper;

import org.fos.kafka.model.avro.order.PaymentRequestAvroModel;
import org.fos.kafka.model.avro.order.PaymentResponseAvroModel;
import org.fos.kafka.model.avro.order.PaymentStatus;
import org.fos.payment.app.service.dto.PaymentRequest;
import org.fos.payment.domain.core.entity.Payment;
import org.fos.payment.domain.core.event.PaymentCancelledEvent;
import org.fos.payment.domain.core.event.PaymentCompletedEvent;
import org.fos.payment.domain.core.event.PaymentEvent;
import org.fos.payment.domain.core.event.PaymentFailedEvent;
import org.fos.payment.domain.core.valueobject.PaymentOrderStatus;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentCompleteEventToPaymentResponseAvroModel(PaymentCompletedEvent paymentCompletedEvent) {
        return getPaymentResponseAvroModel(paymentCompletedEvent.getPayment(), paymentCompletedEvent.getCreatedAt(), paymentCompletedEvent.getFailureMessages(), paymentCompletedEvent);
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent) {
        return getPaymentResponseAvroModel(paymentCancelledEvent.getPayment(), paymentCancelledEvent.getCreatedAt(), paymentCancelledEvent.getFailureMessages(), paymentCancelledEvent);
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent paymentFailedEvent) {
        return getPaymentResponseAvroModel(paymentFailedEvent.getPayment(), paymentFailedEvent.getCreatedAt(), paymentFailedEvent.getFailureMessages(), paymentFailedEvent);
    }

    private PaymentResponseAvroModel getPaymentResponseAvroModel(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, PaymentEvent paymentCancelledEvent) {
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(payment.getId().getValue().toString())
                .setCustomerId(payment.getCustomerId().getValue().toString())
                .setOrderId(payment.getOrderId().getValue().toString())
                .setPrice(payment.getPrice().amount())
                .setCreatedAt(createdAt.toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payment.getPaymentStatus().name()))
                .setFailureMessages(failureMessages)
                .build();
    }

    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel paymentRequestAvroModel) {
        return PaymentRequest.builder()
                .id(paymentRequestAvroModel.getId())
                .sagaId(paymentRequestAvroModel.getId())
                .customerId(paymentRequestAvroModel.getCustomerId())
                .orderId(paymentRequestAvroModel.getOrderId())
                .price(paymentRequestAvroModel.getPrice())
                .createdAt(paymentRequestAvroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(paymentRequestAvroModel.getPaymentOrderStatus().name()))
                .build();
    }
}
