package org.fos.payment.domain.core.entity;

import org.fos.common.domain.entity.BaseEntity;
import org.fos.common.domain.valueobject.CustomerId;
import org.fos.common.domain.valueobject.Money;
import org.fos.payment.domain.core.valueobject.CreditHistoryId;
import org.fos.payment.domain.core.valueobject.TransactionType;

public class CreditHistory extends BaseEntity<CreditHistoryId> {
    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    private CreditHistory(Builder builder) {
        setId(builder.id);
        customerId = builder.customerId;
        amount = builder.amount;
        transactionType = builder.transactionType;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CreditHistoryId id;
        private CustomerId customerId;
        private Money amount;
        private TransactionType transactionType;

        private Builder() {
        }

        public Builder id(CreditHistoryId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(this);
        }
    }
}
