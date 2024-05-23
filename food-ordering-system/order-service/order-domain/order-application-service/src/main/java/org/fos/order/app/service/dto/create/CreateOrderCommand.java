package org.fos.order.app.service.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderCommand(@NotNull UUID customerId, @NotNull UUID restaurantId, @NotNull BigDecimal price,
                                 @NotNull List<OrderItem> items, @NotNull OrderAddress address) {
}
