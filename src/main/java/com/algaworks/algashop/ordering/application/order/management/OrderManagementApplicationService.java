package com.algaworks.algashop.ordering.application.order.management;

import com.algaworks.algashop.ordering.domain.order.Order;
import com.algaworks.algashop.ordering.domain.order.OrderId;
import com.algaworks.algashop.ordering.domain.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

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
