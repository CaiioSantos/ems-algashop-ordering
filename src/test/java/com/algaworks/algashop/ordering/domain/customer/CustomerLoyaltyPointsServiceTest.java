package com.algaworks.algashop.ordering.domain.customer;

import com.algaworks.algashop.ordering.domain.order.Order;
import com.algaworks.algashop.ordering.domain.order.OrderStatus;
import com.algaworks.algashop.ordering.domain.order.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.product.ProductTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomerLoyaltyPointsServiceTest {

    CustomerLoyaltyPointsService customerLoyaltyPointsService = new CustomerLoyaltyPointsService();

    @Test
    public void givenValidCUstomerAndOrder_WhenAddingPoints_ShouldAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();

        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();

        customerLoyaltyPointsService.addPoints(customer,order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo((new LoyaltyPoints(30)));
    }

    @Test
    public void givenValidCUstomerAndOrderWitchLowTotalAmount_WhenAddingPoints_ShouldNotAccumulate() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();

        Order order = OrderTestDataBuilder.anOrder().withItems(false).status(OrderStatus.DRAFT).build();
        order.addItem(product, new Quantity(1));
        order.place();
        order.markAsPaid();
        order.markAsReady();

        customerLoyaltyPointsService.addPoints(customer,order);

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo((new LoyaltyPoints(0)));
    }
}