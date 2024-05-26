package org.fos.restaurant.app.service.ports.input.listener;

import org.fos.restaurant.app.service.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {
    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
