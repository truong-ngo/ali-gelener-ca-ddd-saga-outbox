package org.fos.order.data.access.order.adapter;

import lombok.RequiredArgsConstructor;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.data.access.order.mapper.OrderDataAccessMapper;
import org.fos.order.data.access.order.repository.OrderJpaRepository;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.valueobject.TrackingId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository
                .findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}
