package org.fos.order.app.service.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.fos.common.domain.valueobject.OrderApprovalStatus;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String restaurantId;
    private Instant createAt;
    private OrderApprovalStatus approvalStatus;
    private List<String> failureMessages;
}
