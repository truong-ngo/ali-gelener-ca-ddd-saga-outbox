package org.fos.payment.domain.core;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.payment.domain.core.entity.CreditEntry;
import org.fos.payment.domain.core.entity.CreditHistory;
import org.fos.payment.domain.core.entity.Payment;
import org.fos.payment.domain.core.event.PaymentCancelledEvent;
import org.fos.payment.domain.core.event.PaymentCompletedEvent;
import org.fos.payment.domain.core.event.PaymentEvent;
import org.fos.payment.domain.core.event.PaymentFailedEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentEvent validateAndInitializePayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages, DomainEventPublisher<PaymentCompletedEvent> completePublisher, DomainEventPublisher<PaymentFailedEvent> failurePublisher);
    PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages, DomainEventPublisher<PaymentCancelledEvent> cancelledPublisher, DomainEventPublisher<PaymentFailedEvent> failurePublisher);
}
