package org.fos.payment.app.service.ports.output.message.publisher;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.payment.domain.core.event.PaymentFailedEvent;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {

}
