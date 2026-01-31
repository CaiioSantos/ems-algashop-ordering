package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.customer.CustomerId;

import java.time.OffsetDateTime;

public record OrderReadyEvent(CustomerId customerId, OrderId orderId, OffsetDateTime readyAt) {
}
