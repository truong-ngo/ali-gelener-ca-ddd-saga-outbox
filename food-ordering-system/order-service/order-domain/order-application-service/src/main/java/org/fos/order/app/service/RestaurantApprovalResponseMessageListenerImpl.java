package org.fos.order.app.service;

import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import org.fos.order.app.service.dto.message.RestaurantApprovalResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {

    @Override
    public void orderApproved(RestaurantApprovalResponse response) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse response) {

    }
}
