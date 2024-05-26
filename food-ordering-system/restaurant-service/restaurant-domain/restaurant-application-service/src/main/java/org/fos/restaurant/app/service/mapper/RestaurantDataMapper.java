package org.fos.restaurant.app.service.mapper;

import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.OrderId;
import org.fos.common.domain.valueobject.OrderStatus;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.restaurant.app.service.dto.RestaurantApprovalRequest;
import org.fos.restaurant.domain.core.entity.OrderDetail;
import org.fos.restaurant.domain.core.entity.Product;
import org.fos.restaurant.domain.core.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {
    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        return Restaurant.builder()
                .id(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .id(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .products(restaurantApprovalRequest.getProducts().stream().map(
                                p -> Product.builder()
                                        .id(p.getId())
                                        .quantity(p.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build())
                .build();
    }
}
