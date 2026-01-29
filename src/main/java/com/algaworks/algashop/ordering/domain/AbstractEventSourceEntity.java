package com.algaworks.algashop.ordering.domain;

import java.util.*;

public abstract class AbstractEventSourceEntity implements DomainEventSource {

    protected final List<Object> domainEvents = new ArrayList<>();

    protected void publishDomainEvent(Object event) {
        Objects.requireNonNull(event);
        this.domainEvents.add(event);
    }

    @Override
    public List<Object> domainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    @Override
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
