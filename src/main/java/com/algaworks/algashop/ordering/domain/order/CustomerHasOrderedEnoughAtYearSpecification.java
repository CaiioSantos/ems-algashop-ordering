package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.Specification;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
public class CustomerHasOrderedEnoughAtYearSpecification implements Specification<Customer> {

    private final Orders orders;
    private final long  expectedOrderCount;

    @Override
    public boolean isSatisfiedBy(Customer customer) {
        return orders.salesQuantityByCustomerInYear(
                customer.id(),
                Year.now()
        ) >= expectedOrderCount ;
    }
}
