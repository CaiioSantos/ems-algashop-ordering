package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer;

import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;

import java.util.Optional;
import java.util.UUID;

public interface CustomerPersistenceEntityQueries {
    Optional<CustomerOutput> findByIdAsOutput(UUID customerId);
}
