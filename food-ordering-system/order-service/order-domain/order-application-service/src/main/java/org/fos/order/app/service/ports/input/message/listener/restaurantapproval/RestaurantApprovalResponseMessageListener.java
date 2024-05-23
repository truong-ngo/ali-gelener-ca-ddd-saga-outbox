package org.fos.order.app.service.ports.input.message.listener.restaurantapproval;

import org.fos.order.app.service.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessageListener {
    void orderApproved(RestaurantApprovalResponse response);
    void orderRejected(RestaurantApprovalResponse response);
}
