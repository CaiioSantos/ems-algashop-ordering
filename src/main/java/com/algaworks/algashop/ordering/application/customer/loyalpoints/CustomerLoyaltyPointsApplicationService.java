package com.algaworks.algashop.ordering.application.customer.loyalpoints;

import com.algaworks.algashop.ordering.domain.customer.*;
import com.algaworks.algashop.ordering.domain.order.Order;
import com.algaworks.algashop.ordering.domain.order.OrderId;
import com.algaworks.algashop.ordering.domain.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerLoyaltyPointsApplicationService {

    private final CustomerLoyaltyPointsService customerLoyaltyPointsService;
    private final Orders orders;
    private final Customers customers;

    @Transactional
    public void addLoyaltyPoints(UUID customerId, String orderId) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(orderId);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Order order = orders.ofId(new OrderId(orderId))
                .orElseThrow(OrderNotFoundException::new);

        customerLoyaltyPointsService.addPoints(customer,order);
        customers.add(customer);

    }
}
