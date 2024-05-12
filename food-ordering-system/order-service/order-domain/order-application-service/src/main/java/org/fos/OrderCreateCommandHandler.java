package org.fos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.dto.create.CreateOrderCommand;
import org.fos.dto.create.CreateOrderResponse;
import org.fos.mapper.OrderDataMapper;
import org.fos.orderservicedomain.OrderDomainService;
import org.fos.orderservicedomain.entity.Customer;
import org.fos.orderservicedomain.entity.Order;
import org.fos.orderservicedomain.entity.Restaurant;
import org.fos.orderservicedomain.event.OrderCreatedEvent;
import org.fos.orderservicedomain.exception.OrderDomainException;
import org.fos.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.fos.ports.output.repository.CustomerRepository;
import org.fos.ports.output.repository.OrderRepository;
import org.fos.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }
}
