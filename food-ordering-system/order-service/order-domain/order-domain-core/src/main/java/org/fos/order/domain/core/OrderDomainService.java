package org.fos.order.domain.core;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.order.domain.core.entity.Restaurant;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.event.OrderCancelledEvent;
import org.fos.order.domain.core.event.OrderCreatedEvent;
import org.fos.order.domain.core.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitialize(Order order, Restaurant restaurant, DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher);
    OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages, DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher);
    void cancelOrder(Order order, List<String> failureMessages);
}
