package org.fos.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.fos.domain.valueobject.OrderStatus;

import java.util.UUID;

@Builder
@AllArgsConstructor
public record CreateOrderResponse(@NotNull UUID trackingId, @NotNull OrderStatus status, @NotNull String message) {
}
