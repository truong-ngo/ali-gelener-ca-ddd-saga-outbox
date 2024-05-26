package org.fos.payment.app.service.ports.input.message.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.common.domain.valueobject.CustomerId;
import org.fos.payment.app.service.dto.PaymentRequest;
import org.fos.payment.app.service.exception.PaymentApplicationServiceException;
import org.fos.payment.app.service.mapper.PaymentDataMapper;
import org.fos.payment.app.service.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import org.fos.payment.app.service.ports.output.message.publisher.PaymentCompleteMessagePublisher;
import org.fos.payment.app.service.ports.output.message.publisher.PaymentFailedMessagePublisher;
import org.fos.payment.app.service.ports.output.repository.CreditEntryRepository;
import org.fos.payment.app.service.ports.output.repository.CreditHistoryRepository;
import org.fos.payment.app.service.ports.output.repository.PaymentRepository;
import org.fos.payment.domain.core.PaymentDomainService;
import org.fos.payment.domain.core.entity.CreditEntry;
import org.fos.payment.domain.core.entity.CreditHistory;
import org.fos.payment.domain.core.entity.Payment;
import org.fos.payment.domain.core.event.PaymentEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompleteMessagePublisher paymentCompleteMessagePublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledMessagePublisher;
    private final PaymentFailedMessagePublisher paymentFailedMessagePublisher;

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitializePayment(payment, creditEntry, creditHistories, failureMessages, paymentCompleteMessagePublisher, paymentFailedMessagePublisher);
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentOpt.isEmpty()) {
            log.error("Could not find payment for order: {}", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Could not find payment for order: " + paymentRequest.getOrderId());
        }
        Payment payment = paymentOpt.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages, paymentCancelledMessagePublisher, paymentFailedMessagePublisher);
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
        return paymentEvent;
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
            log.error("Could not find credit history for customer: {}", customerId);
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " + customerId);
        }
        return creditHistories.get();
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer: {}", customerId);
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " + customerId);
        }
        return creditEntry.get();
    }
}
