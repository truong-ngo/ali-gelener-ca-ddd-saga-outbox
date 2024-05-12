package org.fos;

import org.fos.domain.event.DomainEvent;
import org.fos.domain.event.publisher.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;


public abstract class ApplicationDomainEventPublisher<T extends DomainEvent<?>> implements ApplicationEventPublisherAware, DomainEventPublisher<T> {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(T domainEvent) {
        this.applicationEventPublisher.publishEvent(domainEvent);
        postPublishEventHandler(domainEvent);
    }

    public abstract void postPublishEventHandler(T domainEvent);

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
