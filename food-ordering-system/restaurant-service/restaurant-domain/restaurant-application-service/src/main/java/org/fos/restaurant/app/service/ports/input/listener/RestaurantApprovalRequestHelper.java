package org.fos.restaurant.app.service.ports.input.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.valueobject.OrderId;
import org.fos.restaurant.app.service.dto.RestaurantApprovalRequest;
import org.fos.restaurant.app.service.mapper.RestaurantDataMapper;
import org.fos.restaurant.app.service.ports.output.publisher.OrderApprovedMessagePublisher;
import org.fos.restaurant.app.service.ports.output.publisher.OrderRejectedMessagePublisher;
import org.fos.restaurant.app.service.ports.output.repository.OrderApprovalRepository;
import org.fos.restaurant.app.service.ports.output.repository.RestaurantRepository;
import org.fos.restaurant.domain.core.entity.Restaurant;
import org.fos.restaurant.domain.core.event.OrderApprovalEvent;
import org.fos.restaurant.domain.core.exception.RestaurantNotFoundException;
import org.fos.restaurant.domain.service.RestaurantDomainService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestHelper {
    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest) {
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant, failureMessages, orderApprovedMessagePublisher, orderRejectedMessagePublisher);
        orderApprovalRepository.save(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOptional.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found");
            throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() + " not found");
        }
        Restaurant result = restaurantOptional.get();
        restaurant.setActive(result.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product -> {
            result.getOrderDetail().getProducts().forEach(p -> {
                if (p.getId().equals(product.getId())) {
                    product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
                }
            });
        });
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));
        return restaurant;
    }
}
