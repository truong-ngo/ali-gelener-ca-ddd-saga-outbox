package org.fos.payment.domain.core.event;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class PaymentCancelledEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCancelledEvent> publisher;

    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt, DomainEventPublisher<PaymentCancelledEvent> publisher) {
        super(payment, createdAt, new ArrayList<>());
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
