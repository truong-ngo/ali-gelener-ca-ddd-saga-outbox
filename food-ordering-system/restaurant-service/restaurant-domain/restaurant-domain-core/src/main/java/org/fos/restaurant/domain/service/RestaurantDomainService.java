package org.fos.restaurant.domain.service;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.restaurant.domain.core.entity.Restaurant;
import org.fos.restaurant.domain.core.event.OrderApprovalEvent;
import org.fos.restaurant.domain.core.event.OrderApprovedEvent;
import org.fos.restaurant.domain.core.event.OrderRejectedEvent;

import java.util.List;

public interface RestaurantDomainService {
    OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages, DomainEventPublisher<OrderApprovedEvent> approvedPublisher, DomainEventPublisher<OrderRejectedEvent> rejectedPublisher);

}
