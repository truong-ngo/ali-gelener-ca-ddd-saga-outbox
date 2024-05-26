package org.fos.payment.app.service.ports.input.message.listener;

import org.fos.payment.app.service.dto.PaymentRequest;

public interface PaymentRequestMessageListener {
    void completePayment(PaymentRequest paymentRequest);
    void cancelPayment(PaymentRequest paymentRequest);
}
