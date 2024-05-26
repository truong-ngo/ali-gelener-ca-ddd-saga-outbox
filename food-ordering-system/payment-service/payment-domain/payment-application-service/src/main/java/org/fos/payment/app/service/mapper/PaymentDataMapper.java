package org.fos.payment.app.service.mapper;

import org.fos.common.domain.valueobject.CustomerId;
import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.OrderId;
import org.fos.payment.app.service.dto.PaymentRequest;
import org.fos.payment.domain.core.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {
    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
