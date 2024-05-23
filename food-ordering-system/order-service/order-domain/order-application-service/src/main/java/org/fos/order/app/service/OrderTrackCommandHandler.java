package org.fos.order.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.dto.track.TrackOrderQuery;
import org.fos.order.app.service.dto.track.TrackOrderResponse;
import org.fos.order.app.service.mapper.OrderDataMapper;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.exception.OrderNotFoundException;
import org.fos.order.domain.core.valueobject.TrackingId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> order = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.trackingId()));
        if (order.isEmpty()) {
            log.warn("Could not find Order with tracking id: {}", trackOrderQuery.trackingId());
            throw new OrderNotFoundException("Could not find Order with tracking id: " + trackOrderQuery.trackingId().toString());
        }
        return orderDataMapper.orderToTrackOrderResponse(order.get());
    }
}
