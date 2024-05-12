package org.fos.ports.output.message.publisher.payment;

import org.fos.domain.event.publisher.DomainEventPublisher;
import org.fos.orderservicedomain.event.OrderCancelledEvent;
import org.fos.orderservicedomain.event.OrderCreatedEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
