package com.algaworks.algashop.ordering.application.order.query;

import com.algaworks.algashop.ordering.application.utility.PageFilter;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.customer.Customers;
import com.algaworks.algashop.ordering.domain.order.Order;
import com.algaworks.algashop.ordering.domain.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.order.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.order.Orders;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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

    @Test
    public void shouldFilterByPage() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).withItems(false).customerId(customer.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).customerId(customer.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).customerId(customer.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.READY).customerId(customer.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).customerId(customer.id()).build());

        Page<OrderSummaryOutput> page = orderQueryService.filter(new OrderFilter(3, 0));

        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(3);
    }

    @Test
    public void shouldFilterByCostumerId() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).withItems(false).customerId(customer.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).customerId(customer.id()).build());

        Customer customer2 = CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).build();
        customers.add(customer2);
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).customerId(customer2.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.READY).customerId(customer2.id()).build());
        orders.add(OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).customerId(customer2.id()).build());

        OrderFilter filter = new OrderFilter();
        filter.setCustomerId(customer.id().value());
        Page<OrderSummaryOutput> page = orderQueryService.filter(filter);

        Assertions.assertThat(page.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(2);
    }

}