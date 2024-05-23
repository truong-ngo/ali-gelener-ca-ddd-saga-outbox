package org.fos.order.app.service.ports.output.repository;

import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}
