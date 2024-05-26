package org.fos.restaurant.domain.core.entity;

import org.fos.common.domain.entity.BaseEntity;
import org.fos.common.domain.valueobject.OrderApprovalStatus;
import org.fos.common.domain.valueobject.OrderId;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.restaurant.domain.core.valueobject.OrderApprovalId;

public class OrderApproval extends BaseEntity<OrderApprovalId> {
    private final RestaurantId restaurantId;
    private final OrderId orderId;
    private final OrderApprovalStatus orderApprovalStatus;

    private OrderApproval(Builder builder) {
        setId(builder.id);
        restaurantId = builder.restaurantId;
        orderId = builder.orderId;
        orderApprovalStatus = builder.orderApprovalStatus;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getOrderApprovalStatus() {
        return orderApprovalStatus;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private OrderApprovalId id;
        private RestaurantId restaurantId;
        private OrderId orderId;
        private OrderApprovalStatus orderApprovalStatus;

        private Builder() {
        }

        public Builder id(OrderApprovalId val) {
            id = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder orderApprovalStatus(OrderApprovalStatus val) {
            orderApprovalStatus = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
