package com.algaworks.algashop.ordering.core.application.order;

import com.algaworks.algashop.ordering.core.domain.model.order.Order;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.order.Orders;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForManagingCustomers;
import com.algaworks.algashop.ordering.core.ports.in.order.ForManagingOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService implements ForManagingOrders {

    private final Orders orders;

    public void cancel(String orderId) {
        Objects.requireNonNull(orderId);

        Order order = orders.ofId(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException());
        order.cancel();
        orders.add(order);

    }
    public void markAsPaid(String orderId) {
        Objects.requireNonNull(orderId);

        Order order = orders.ofId(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException());
        order.markAsPaid();
        orders.add(order);
    }

    public void markAsReady(String orderId) {
        Objects.requireNonNull(orderId);

        Order order = orders.ofId(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException());
        order.markAsReady();
        orders.add(order);
    }
}
