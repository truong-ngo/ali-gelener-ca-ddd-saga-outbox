package org.fos.restaurant.app.service.ports.output.publisher;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.restaurant.domain.core.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}
