package org.fos.order.app.service;

import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.ports.input.message.listener.payment.PaymentResponseMessageListener;
import org.fos.order.app.service.dto.message.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}