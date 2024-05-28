package org.fos.order.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import org.fos.order.app.service.dto.message.RestaurantApprovalResponse;
import org.fos.order.domain.core.event.OrderCancelledEvent;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    private final OrderApprovalSaga orderApprovalSaga;

    @Override
    public void orderApproved(RestaurantApprovalResponse response) {
        orderApprovalSaga.process(response);
        log.info("Order is approved for order id: {}", response.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {
        OrderCancelledEvent orderCancelledEvent = orderApprovalSaga.rollback(response);
        log.info("Publishing order cancelled for order id: {} with failure messages: {}", response.getOrderId(), String.join(",", response.getFailureMessages()));
        orderCancelledEvent.fire();
    }
}
