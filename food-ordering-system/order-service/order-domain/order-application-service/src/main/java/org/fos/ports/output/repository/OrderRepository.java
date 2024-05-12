package org.fos.ports.output.repository;

import org.fos.orderservicedomain.entity.Order;
import org.fos.orderservicedomain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByTrackingId(TrackingId trackingId);
}
