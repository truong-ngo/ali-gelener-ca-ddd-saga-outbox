package org.fos.dto.track;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TrackOrderQuery(@NotNull UUID trackingId) {
}
