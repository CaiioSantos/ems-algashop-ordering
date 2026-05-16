package com.algaworks.algashop.ordering.core.ports.in.customer;

import java.util.UUID;

public interface ForAddingLoyaltypoints {

    void addLoyaltyPoints(UUID customerId, String orderId);
}
