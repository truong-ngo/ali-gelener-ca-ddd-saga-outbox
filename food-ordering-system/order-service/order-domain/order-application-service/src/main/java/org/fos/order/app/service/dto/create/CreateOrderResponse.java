package org.fos.order.app.service.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.fos.common.domain.valueobject.OrderStatus;

import java.util.UUID;

@Builder
public record CreateOrderResponse(@NotNull UUID trackingId, @NotNull OrderStatus status, @NotNull String message) {
}
