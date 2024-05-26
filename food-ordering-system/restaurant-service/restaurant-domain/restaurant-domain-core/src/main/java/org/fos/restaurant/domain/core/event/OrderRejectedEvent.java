package org.fos.restaurant.domain.core.event;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.restaurant.domain.core.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderRejectedEvent> publisher;

    public OrderRejectedEvent(OrderApproval orderApproval, RestaurantId restaurantId, List<String> failureMessages, ZonedDateTime createdAt, DomainEventPublisher<OrderRejectedEvent> publisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
