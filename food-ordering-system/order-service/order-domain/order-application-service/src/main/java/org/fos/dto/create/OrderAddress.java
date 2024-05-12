package org.fos.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public record OrderAddress(@NotNull @Max(value = 50) String street, @NotNull @Max(value = 10) String postalCode,
                           @NotNull @Max(value = 50) String city) {
}
