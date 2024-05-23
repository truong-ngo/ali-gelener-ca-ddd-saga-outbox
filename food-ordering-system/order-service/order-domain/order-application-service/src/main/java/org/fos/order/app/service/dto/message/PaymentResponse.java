package org.fos.order.app.service.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.fos.common.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String paymentId;
    private String customerId;
    private BigDecimal price;
    private Instant createAt;
    private PaymentStatus paymentStatus;
    private List<String> failureMessages;
}
