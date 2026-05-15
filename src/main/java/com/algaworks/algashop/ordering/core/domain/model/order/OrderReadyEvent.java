package com.algaworks.algashop.ordering.core.domain.model.order;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderReadyEvent(CustomerId customerId, OrderId orderId, OffsetDateTime readyAt) {
}
