package org.fos.payment.domain.core.event;

import org.fos.common.domain.event.DomainEvent;
import org.fos.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class PaymentEvent implements DomainEvent<Payment> {
    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
