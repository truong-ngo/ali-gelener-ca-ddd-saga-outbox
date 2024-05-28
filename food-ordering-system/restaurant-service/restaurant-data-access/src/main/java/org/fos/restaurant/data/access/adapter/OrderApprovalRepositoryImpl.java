package org.fos.restaurant.data.access.adapter;

import lombok.RequiredArgsConstructor;
import org.fos.restaurant.app.service.ports.output.repository.OrderApprovalRepository;
import org.fos.restaurant.data.access.mapper.RestaurantDataAccessMapper;
import org.fos.restaurant.data.access.repository.OrderApprovalJpaRepository;
import org.fos.restaurant.domain.core.entity.OrderApproval;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {
    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }
}
