package org.fos.common.app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record ErrorDTO(String code, String message) {
}
