package org.fos.payment.domain.core.event;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.payment.domain.core.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentFailedEvent> publisher;

    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, DomainEventPublisher<PaymentFailedEvent> publisher) {
        super(payment, createdAt, failureMessages);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
