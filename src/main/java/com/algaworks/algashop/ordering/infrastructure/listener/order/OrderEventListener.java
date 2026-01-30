package com.algaworks.algashop.ordering.infrastructure.listener.order;

import com.algaworks.algashop.ordering.domain.order.OrderCanceledEvent;
import com.algaworks.algashop.ordering.domain.order.OrderPaidEvent;
import com.algaworks.algashop.ordering.domain.order.OrderPlacedEvent;
import com.algaworks.algashop.ordering.domain.order.OrderReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @EventListener
    public void listen(OrderPlacedEvent event) {

    }

    @EventListener
    public void listen(OrderPaidEvent event) {

    }

    @EventListener
    public void listen(OrderReadyEvent event) {

    }

    @EventListener
    public void listen(OrderCanceledEvent event) {

    }
}
