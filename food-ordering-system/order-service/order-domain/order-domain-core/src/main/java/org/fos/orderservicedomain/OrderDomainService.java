package org.fos.orderservicedomain;

import org.fos.orderservicedomain.entity.Order;
import org.fos.orderservicedomain.entity.Restaurant;
import org.fos.orderservicedomain.event.OrderCancelledEvent;
import org.fos.orderservicedomain.event.OrderCreatedEvent;
import org.fos.orderservicedomain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitialize(Order order, Restaurant restaurant);
    OrderPaidEvent payOrder(Order order);
    void approveOrder(Order order);
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
    void cancelOrder(Order order, List<String> failureMessages);
}
