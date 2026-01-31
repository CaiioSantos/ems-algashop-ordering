package com.algaworks.algashop.ordering.infrastructure.listener.shoppingcart;

import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartCreatedEvent;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartEmptiedEvent;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartItemAddedEvent;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartItemRemovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartEventListener {

    @EventListener
    public void listen(ShoppingCartCreatedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartEmptiedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartItemAddedEvent event) {

    }

    @EventListener
    public void listen(ShoppingCartItemRemovedEvent event) {

    }
}
