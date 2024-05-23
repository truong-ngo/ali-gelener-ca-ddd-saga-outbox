package org.fos.order.app.service.ports.input.message.listener.payment;

import org.fos.order.app.service.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCancelled(PaymentResponse paymentResponse);
}
