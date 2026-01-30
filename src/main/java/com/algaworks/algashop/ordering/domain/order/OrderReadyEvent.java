package com.algaworks.algashop.ordering.domain.order;

import java.time.OffsetDateTime;

public record OrderReadyEvent(OrderId orderId, OffsetDateTime readyAt) {
}
