package org.fos.order.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.ports.input.message.listener.payment.PaymentResponseMessageListener;
import org.fos.order.app.service.dto.message.PaymentResponse;
import org.fos.order.domain.core.event.OrderPaidEvent;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent orderPaidEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Publishing OrderPaidEvent for order id: {}", paymentResponse.getOrderId());
        orderPaidEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is rollback for order id: {} with failure message: {}", paymentResponse.getOrderId(), String.join(",", paymentResponse.getFailureMessages()));
    }
}