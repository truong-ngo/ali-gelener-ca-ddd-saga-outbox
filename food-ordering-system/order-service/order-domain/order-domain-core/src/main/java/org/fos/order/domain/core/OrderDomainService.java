package org.fos.order.domain.core;

import org.fos.order.domain.core.entity.Restaurant;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.event.OrderCancelledEvent;
import org.fos.order.domain.core.event.OrderCreatedEvent;
import org.fos.order.domain.core.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitialize(Order order, Restaurant restaurant);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);
}
