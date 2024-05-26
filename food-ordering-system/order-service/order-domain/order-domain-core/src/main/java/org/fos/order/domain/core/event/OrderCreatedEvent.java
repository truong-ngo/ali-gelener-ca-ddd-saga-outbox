package org.fos.order.domain.core.event;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.order.domain.core.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCreatedEvent> publisher;

    public OrderCreatedEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCreatedEvent> publisher) {
        super(order, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
