package org.fos.payment.app.service.ports.output.repository;

import org.fos.common.domain.valueobject.CustomerId;
import org.fos.payment.domain.core.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
