package org.fos.payment.app.service.ports.output.message.publisher;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.payment.domain.core.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
