package org.fos.payment.domain.core.entity;

import org.fos.common.domain.entity.BaseEntity;
import org.fos.common.domain.valueobject.CustomerId;
import org.fos.common.domain.valueobject.Money;
import org.fos.payment.domain.core.valueobject.CreditEntryId;

public class CreditEntry extends BaseEntity<CreditEntryId> {
    private final CustomerId customerId;
    private Money totalCreditAmount;

    private CreditEntry(Builder builder) {
        setId(builder.id);
        customerId = builder.customerId;
        setTotalCreditAmount(builder.totalCreditAmount);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public void setTotalCreditAmount(Money totalCreditAmount) {
        this.totalCreditAmount = totalCreditAmount;
    }

    public void addCreditAmount(Money amount) {
        totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        totalCreditAmount = totalCreditAmount.subtract(amount);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CreditEntryId id;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public Builder id(CreditEntryId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
