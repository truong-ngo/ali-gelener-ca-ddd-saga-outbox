package org.fos.order.app.service.mapper;

import org.fos.order.app.service.dto.create.CreateOrderCommand;
import org.fos.order.app.service.dto.create.CreateOrderResponse;
import org.fos.order.app.service.dto.create.OrderAddress;
import org.fos.order.app.service.dto.track.TrackOrderResponse;
import org.fos.common.domain.valueobject.CustomerId;
import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.ProductId;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.entity.OrderItem;
import org.fos.order.domain.core.entity.Product;
import org.fos.order.domain.core.entity.Restaurant;
import org.fos.order.domain.core.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .id(new RestaurantId(createOrderCommand.restaurantId()))
                .products(createOrderCommand.items().stream().map(ot -> new Product(new ProductId(ot.productId()))).toList())
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.customerId()))
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.address()))
                .price(new Money(createOrderCommand.price()))
                .items(orderItemToOrderItemEntity(createOrderCommand.items()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .trackingId(order.getTrackingId().getValue())
                .status(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .trackingId(order.getTrackingId().getValue())
                .status(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemToOrderItemEntity(List<org.fos.order.app.service.dto.create.OrderItem> items) {
        return items.stream().map(ot -> OrderItem.builder()
                .product(new Product(new ProductId(ot.productId())))
                .price(new Money(ot.price()))
                .quantity(ot.quantity())
                .subTotal(new Money(ot.subTotal()))
                .build()
        ).toList();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(), address.street(), address.postalCode(), address.city());
    }
}
