package com.algaworks.algashop.ordering.core.application.customer;

import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerFilter;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerSummaryOutput;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForQueryingCustomers;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForObtainingCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerQueryService implements ForQueryingCustomers {

    private final ForObtainingCustomers obtainingCustomers;

    @Override
    public CustomerOutput findById(UUID customerId) {
        return obtainingCustomers.findById(customerId);
    }

    @Override
    public Page<CustomerSummaryOutput> filter(CustomerFilter filter) {
        return obtainingCustomers.filter(filter);
    }
}
