package org.fos.order.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.dto.create.CreateOrderCommand;
import org.fos.order.app.service.dto.create.CreateOrderResponse;
import org.fos.order.app.service.mapper.OrderDataMapper;
import org.fos.order.app.service.ports.output.repository.CustomerRepository;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.app.service.ports.output.repository.RestaurantRepository;
import org.fos.order.domain.core.OrderDomainService;
import org.fos.order.domain.core.entity.Customer;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.entity.Restaurant;
import org.fos.order.domain.core.event.OrderCreatedEvent;
import org.fos.order.domain.core.exception.OrderDomainException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.customerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitialize(order, restaurant);
        Order result = saveOrder(order);
        log.info("Order is created with id: {}", result.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(order, "Order Created Successfully");
    }

    private Restaurant checkRestaurant(CreateOrderCommand command) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(command);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with id: {}", restaurant.getId());
            throw new OrderDomainException("Could not find restaurant with id:" + restaurant.getId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with id: {}", customerId);
            throw new OrderDomainException("Could not find customer with id:" + customerId.toString());
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("Could not save order");
            throw new OrderDomainException("Could not save order");
        }
        log.info("Order is saved with id: {}    ", orderResult.getId().getValue());
        return orderResult;
    }
}
