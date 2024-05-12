package org.fos.ports.output.message.publisher.payment;

import org.fos.domain.event.publisher.DomainEventPublisher;
import org.fos.orderservicedomain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
