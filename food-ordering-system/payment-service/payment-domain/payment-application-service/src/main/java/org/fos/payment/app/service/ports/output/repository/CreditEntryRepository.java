package org.fos.payment.app.service.ports.output.repository;

import org.fos.common.domain.valueobject.CustomerId;
import org.fos.payment.domain.core.entity.CreditEntry;

import java.util.Optional;

public interface CreditEntryRepository {
    CreditEntry save(CreditEntry creditEntry);

    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
