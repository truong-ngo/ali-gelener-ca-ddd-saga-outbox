package org.fos;

import org.fos.domain.event.DomainEvent;

public abstract class ApplicationDomainEventListener<T extends DomainEvent<?>> {
    public abstract void process(T domainEvent);
}
