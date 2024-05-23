package org.fos.order.app.service.ports.output.message.publisher.payment;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.order.domain.core.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
