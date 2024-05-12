package org.fos;

import lombok.extern.slf4j.Slf4j;
import org.fos.orderservicedomain.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationOrderCreatedEventPublisher extends ApplicationDomainEventPublisher<OrderCreatedEvent> {

    @Override
    public void postPublishEventHandler(OrderCreatedEvent domainEvent) {
        log.info("OrderCreatedEvent is published for order with id {}", domainEvent.getOrder().getId().getValue());
    }
}
