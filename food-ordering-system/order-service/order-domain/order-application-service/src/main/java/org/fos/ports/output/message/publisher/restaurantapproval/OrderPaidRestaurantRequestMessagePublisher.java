package org.fos.ports.output.message.publisher.restaurantapproval;

import org.fos.domain.event.publisher.DomainEventPublisher;
import org.fos.orderservicedomain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
