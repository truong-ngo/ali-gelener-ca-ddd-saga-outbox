package org.fos.order.app.service.dto.track;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record TrackOrderQuery(@NotNull UUID trackingId) {
}
