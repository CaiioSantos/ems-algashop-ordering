package com.algaworks.algashop.ordering.infrastructure.beans;

import com.algaworks.algashop.ordering.domain.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.order.CustomerHaveFreeShippingSpecification;
import com.algaworks.algashop.ordering.domain.order.Orders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpecificationBeansConfig {

    @Bean
    public CustomerHaveFreeShippingSpecification customerHaveFreeShippingSpecification(Orders orders) {
        return new CustomerHaveFreeShippingSpecification(
                orders,
                new LoyaltyPoints(200),
                2l,
                new LoyaltyPoints(2000)
        );
    }
}
