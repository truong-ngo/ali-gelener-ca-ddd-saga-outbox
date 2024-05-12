package org.fos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.orderservicedomain.event.OrderCreatedEvent;
import org.fos.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationOrderCreatedEventListener extends ApplicationDomainEventListener<OrderCreatedEvent> {

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    @Override
    @TransactionalEventListener
    public void process(OrderCreatedEvent domainEvent) {
        orderCreatedPaymentRequestMessagePublisher.publish(domainEvent);
        log.info("Publish payment request for order with id: {}", domainEvent.getOrder().getId().getValue());
    }
}
