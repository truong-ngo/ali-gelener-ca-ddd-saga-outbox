package org.fos.order.app.service.ports.output.message.publisher.restaurantapproval;

import org.fos.common.domain.event.publisher.DomainEventPublisher;
import org.fos.order.domain.core.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
