package org.fos.payment.domain.core.event;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class PaymentCompletedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCompletedEvent> publisher;

    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt, DomainEventPublisher<PaymentCompletedEvent> publisher) {
        super(payment, createdAt, new ArrayList<>());
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
