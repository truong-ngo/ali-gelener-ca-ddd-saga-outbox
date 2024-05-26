package org.fos.restaurant.domain.core.event;

import org.fos.common.domain.event.DomainEvent;
import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.restaurant.domain.core.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderApprovedEvent> publisher;

    public OrderApprovedEvent(OrderApproval orderApproval, RestaurantId restaurantId, List<String> failureMessages, ZonedDateTime createdAt, DomainEventPublisher<OrderApprovedEvent> publisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
