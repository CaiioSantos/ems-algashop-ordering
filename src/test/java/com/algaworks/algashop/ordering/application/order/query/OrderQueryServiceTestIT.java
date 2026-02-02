package com.algaworks.algashop.ordering.application.order.query;

import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.customer.Customers;
import com.algaworks.algashop.ordering.domain.order.Order;
import com.algaworks.algashop.ordering.domain.order.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.order.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderQueryServiceTestIT {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private Orders orders;

    @Autowired
    private Customers customers;

    @Test
    public void souldFindById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        Order order = OrderTestDataBuilder.anOrder().build();
        orders.add(order);

        OrderDetailOutput orderDetailOutput = orderQueryService.findById(order.id().toString());

        Assertions.assertThat(orderDetailOutput)
                .extracting(
                        OrderDetailOutput::getId,
                        OrderDetailOutput::getTotalAmount
                ).containsExactly(
                        order.id().toString(),
                        order.totalAmount().value()
                );
    }
}