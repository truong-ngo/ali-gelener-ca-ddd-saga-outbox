package org.fos.payment.domain.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.PaymentStatus;
import org.fos.payment.domain.core.entity.CreditEntry;
import org.fos.payment.domain.core.entity.CreditHistory;
import org.fos.payment.domain.core.entity.Payment;
import org.fos.payment.domain.core.event.PaymentCancelledEvent;
import org.fos.payment.domain.core.event.PaymentCompletedEvent;
import org.fos.payment.domain.core.event.PaymentEvent;
import org.fos.payment.domain.core.event.PaymentFailedEvent;
import org.fos.payment.domain.core.valueobject.CreditHistoryId;
import org.fos.payment.domain.core.valueobject.TransactionType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent validateAndInitializePayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages, DomainEventPublisher<PaymentCompletedEvent> completePublisher, DomainEventPublisher<PaymentFailedEvent> failurePublisher) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id {}", payment.getOrderId().getValue().toString());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), completePublisher);
        } else {
            log.error("Payment initiation is failed for order id {}", payment.getOrderId().getValue().toString());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages, failurePublisher);
        }
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);

        Money totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error("Customer with id: {} doesnt have enough credit according to credit history", creditEntry.getCustomerId().getValue());
            failureMessages.add("Customer with id=" + creditEntry.getCustomerId().getValue().toString() + " doesnt have enough credit according to credit history");
        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            log.error("Credit history total is not equal to current credit for customer id {}", creditEntry.getCustomerId().getValue());
            failureMessages.add("Credit history total is not equal to current credit for customer id=" + creditEntry.getCustomerId().getValue().toString());
        }
    }

    private Money getTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(ch -> ch.getTransactionType() == transactionType)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType debit) {
        creditHistories.add(CreditHistory.builder()
                        .id(new CreditHistoryId(UUID.randomUUID()))
                        .customerId(payment.getCustomerId())
                        .amount(payment.getPrice())
                        .transactionType(debit)
                        .build()
        );
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id {} doesn't hava enough credit for payment!", payment.getCustomerId().getValue());
            failureMessages.add("Customer with id=" + payment.getCustomerId().getValue().toString() + " doesn't hava enough credit for payment!");
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages, DomainEventPublisher<PaymentCancelledEvent> cancelledPublisher, DomainEventPublisher<PaymentFailedEvent> failurePublisher) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);
        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id {}", payment.getOrderId().getValue().toString());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), cancelledPublisher);
        } else {
            log.error("Payment cancellation is failed for order id {}", payment.getOrderId().getValue().toString());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of("UTC")), failureMessages, failurePublisher);
        }
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
