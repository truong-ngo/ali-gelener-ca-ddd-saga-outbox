package org.fos.restaurant.app.service.ports.output.repository;

import org.fos.restaurant.domain.core.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
