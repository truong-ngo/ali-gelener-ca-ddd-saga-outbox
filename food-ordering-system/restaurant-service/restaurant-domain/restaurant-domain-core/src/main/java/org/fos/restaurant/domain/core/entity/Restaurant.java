package org.fos.restaurant.domain.core.entity;

import org.fos.common.domain.entity.AggregateRoot;
import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.OrderApprovalStatus;
import org.fos.common.domain.valueobject.OrderStatus;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.restaurant.domain.core.valueobject.OrderApprovalId;

import java.util.List;
import java.util.UUID;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private OrderApproval orderApproval;
    private boolean active;
    private final OrderDetail orderDetail;

    private Restaurant(Builder builder) {
        setId(builder.id);
        setOrderApproval(builder.orderApproval);
        setActive(builder.active);
        orderDetail = builder.orderDetail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public void setOrderApproval(OrderApproval orderApproval) {
        this.orderApproval = orderApproval;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void validateOrder(List<String> failureMessages) {
        if (orderDetail.getOrderStatus() != OrderStatus.PAID) {
            failureMessages.add("Payment is not complete for order: " + orderDetail.getId().getValue().toString());
        }
        Money totalAmount = orderDetail.getProducts().stream().map(p -> {
            if (!p.isAvailable()) {
                failureMessages.add("Product with id: " + p.getId().getValue().toString() + " is not available");
            }
            return p.getPrice().multiply(p.getQuantity());
        }).reduce(Money.ZERO, Money::add);

        if (!totalAmount.equals(orderDetail.getTotalAmount())) {
            failureMessages.add("Total price is not correct for order: " + orderDetail.getId().getValue().toString());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.builder()
                .id(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(orderDetail.getId())
                .orderApprovalStatus(orderApprovalStatus)
                .build();
    }

    public static final class Builder {
        private RestaurantId id;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder id(RestaurantId val) {
            id = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
