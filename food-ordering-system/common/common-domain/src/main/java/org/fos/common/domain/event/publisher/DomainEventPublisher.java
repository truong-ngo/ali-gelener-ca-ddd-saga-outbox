package org.fos.common.domain.event.publisher;

import org.fos.common.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent<?>> {
    void publish(T domainEvent);
}
