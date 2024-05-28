package org.fos.restaurant.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.common.domain.valueobject.OrderApprovalStatus;
import org.fos.restaurant.domain.core.entity.Restaurant;
import org.fos.restaurant.domain.core.event.OrderApprovalEvent;
import org.fos.restaurant.domain.core.event.OrderApprovedEvent;
import org.fos.restaurant.domain.core.event.OrderRejectedEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages, DomainEventPublisher<OrderApprovedEvent> approvedPublisher, DomainEventPublisher<OrderRejectedEvent> rejectedPublisher) {
        restaurant.validateOrder(failureMessages);
        log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue().toString());

        if (failureMessages.isEmpty()) {
            log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue().toString());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(), failureMessages, ZonedDateTime.now(ZoneId.of("UTC")), approvedPublisher);
        } else {
            log.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue().toString());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(), failureMessages, ZonedDateTime.now(ZoneId.of("UTC")), rejectedPublisher);
        }
    }
}
