package org.fos.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.fos.domain.valueobject.OrderStatus;

import java.util.List;
import java.util.UUID;

@Builder
public record TrackOrderResponse(@NotNull UUID trackingId, @NotNull OrderStatus status, List<String> failureMessages) {
}
