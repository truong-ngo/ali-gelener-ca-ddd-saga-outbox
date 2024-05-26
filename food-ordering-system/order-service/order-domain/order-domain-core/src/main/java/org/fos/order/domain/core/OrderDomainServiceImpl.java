package org.fos.order.domain.core;

import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.order.domain.core.entity.Restaurant;
import org.fos.order.domain.core.exception.OrderDomainException;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.entity.Product;
import org.fos.order.domain.core.event.OrderCancelledEvent;
import org.fos.order.domain.core.event.OrderCreatedEvent;
import org.fos.order.domain.core.event.OrderPaidEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitialize(Order order, Restaurant restaurant, DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventPublisher);
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach(ot -> restaurant.getProducts().forEach(rp -> {
            Product currentProduct = ot.getProduct();
            if (currentProduct.equals(rp)) {
                currentProduct.updateWithConfirmNameAndPrice(rp.getName(), rp.getPrice());
            }
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id: " + restaurant.getId().getValue() + " is currently not active");
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher) {
        order.pay();
        log.info("Order with id: {} has been paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderPaidEventPublisher);
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} has been approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages, DomainEventPublisher<OrderCancelledEvent> orderCancelledEventPublisher) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCancelledEventPublisher);
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id: {} has been cancelled", order.getId().getValue());
    }
}
