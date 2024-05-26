package org.fos.payment.app.service.ports.output.repository;

import org.fos.payment.domain.core.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderId);
}
