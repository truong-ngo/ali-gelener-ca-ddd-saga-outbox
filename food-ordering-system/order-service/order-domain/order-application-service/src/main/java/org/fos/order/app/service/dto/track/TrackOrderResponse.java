package org.fos.order.app.service.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.fos.common.domain.valueobject.OrderStatus;

import java.util.List;
import java.util.UUID;

@Builder
public record TrackOrderResponse(@NotNull UUID trackingId, @NotNull OrderStatus status, List<String> failureMessages) {
}
