package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.Repository;
import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;

import java.time.Year;
import java.util.List;

public interface Orders extends Repository<Order, OrderId> {
    List<Order> placedByCustomerInYear(CustomerId customerId, Year year);
    long salesQuantityByCustomerInYear(CustomerId customerId, Year year);
    Money totalSoldForCustomer(CustomerId customerId);
}
