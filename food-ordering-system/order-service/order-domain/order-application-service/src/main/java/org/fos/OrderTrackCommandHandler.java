package org.fos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.dto.track.TrackOrderQuery;
import org.fos.dto.track.TrackOrderResponse;
import org.fos.mapper.OrderDataMapper;
import org.fos.orderservicedomain.entity.Order;
import org.fos.orderservicedomain.exception.OrderNotFoundException;
import org.fos.orderservicedomain.valueobject.TrackingId;
import org.fos.ports.output.repository.OrderRepository;
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
