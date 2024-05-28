package org.fos.order.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.event.EmptyEvent;
import org.fos.order.app.service.dto.message.RestaurantApprovalResponse;
import org.fos.order.app.service.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.domain.core.OrderDomainService;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.event.OrderCancelledEvent;
import org.fos.saga.SagaStep;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;

    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse data) {
        log.info("Approving order with id: {}", data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is approved", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Override
    public OrderCancelledEvent rollback(RestaurantApprovalResponse data) {
        log.info("Cancelling order with id: {}", data.getOrderId());
        Order order = orderSagaHelper.findOrder(data.getOrderId());
        OrderCancelledEvent orderCancelledEvent = orderDomainService.cancelOrderPayment(order, data.getFailureMessages(), orderCancelledPaymentRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelling", order.getId().getValue());
        return orderCancelledEvent;
    }
}
