package org.fos.order.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.valueobject.OrderId;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.exception.OrderNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    public Order findOrder(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if (orderOpt.isEmpty()) {
            log.error("Order with id: {} could not be found", orderId);
            throw new OrderNotFoundException("Order with id: " + orderId + " could not be found");
        }
        return orderOpt.get();
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
