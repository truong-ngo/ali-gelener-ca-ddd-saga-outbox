package org.fos.order.data.access.order.mapper;

import org.fos.common.domain.valueobject.*;
import org.fos.order.data.access.order.entity.OrderAddressEntity;
import org.fos.order.data.access.order.entity.OrderEntity;
import org.fos.order.data.access.order.entity.OrderItemEntity;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.entity.OrderItem;
import org.fos.order.domain.core.entity.Product;
import org.fos.order.domain.core.valueobject.OrderItemId;
import org.fos.order.domain.core.valueobject.StreetAddress;
import org.fos.order.domain.core.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().amount())
                .orderItems(orderItemToOrderItemEntity(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() !=
                        null ?
                        String.join(",", order.getFailureMessages()) : "")
                .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getOrderItems().forEach(ot -> ot.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity entity) {
        return Order.builder()
                .orderId(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .restaurantId(new RestaurantId(entity.getRestaurantId()))
                .trackingId(new TrackingId(entity.getTrackingId()))
                .deliveryAddress(addressEntityToDeliveryAddress(entity.getAddress()))
                .price(new Money(entity.getPrice()))
                .items(orderItemEntityToOrderItem(entity.getOrderItems()))
                .trackingId(new TrackingId(entity.getTrackingId()))
                .orderStatus(entity.getOrderStatus())
                .failureMessages(entity.getFailureMessages().isEmpty() ?
                        new ArrayList<>() :
                        List.of(entity.getFailureMessages().split(",")))
                .build();
    }

    private List<OrderItem> orderItemEntityToOrderItem(List<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .map(e -> OrderItem.builder()
                        .orderItemId(new OrderItemId(e.getId()))
                        .orderId(new OrderId(e.getOrder().getId()))
                        .product(new Product(new ProductId(e.getProductId())))
                        .price(new Money(e.getPrice()))
                        .quantity(e.getQuantity())
                        .subTotal(new Money(e.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return StreetAddress.builder()
                .id(address.getId())
                .street(address.getStreet())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .build();
    }

    private List<OrderItemEntity> orderItemToOrderItemEntity(List<OrderItem> items) {
        return items.stream()
                .map(i -> OrderItemEntity.builder()
                        .id(i.getId().getValue())
                        .productId(i.getProduct().getId().getValue())
                        .price(i.getPrice().amount())
                        .quantity(i.getQuantity())
                        .subTotal(i.getSubTotal().amount())
                        .build())
                .collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.id())
                .street(deliveryAddress.street())
                .postalCode(deliveryAddress.postalCode())
                .city(deliveryAddress.city())
                .build();
    }
}
