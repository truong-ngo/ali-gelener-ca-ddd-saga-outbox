package org.fos.payment.data.access.creditentry.mapper;

import org.fos.common.domain.valueobject.CustomerId;
import org.fos.common.domain.valueobject.Money;
import org.fos.payment.data.access.creditentry.entity.CreditEntryEntity;
import org.fos.payment.domain.core.entity.CreditEntry;
import org.fos.payment.domain.core.valueobject.CreditEntryId;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

    public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntry.builder()
                .id(new CreditEntryId(creditEntryEntity.getId()))
                .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().amount())
                .build();
    }

}
