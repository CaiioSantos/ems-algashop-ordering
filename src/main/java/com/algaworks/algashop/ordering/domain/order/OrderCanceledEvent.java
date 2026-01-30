package com.algaworks.algashop.ordering.domain.order;

import java.time.OffsetDateTime;

public record OrderCanceledEvent(OrderId orderId, OffsetDateTime canceledAt) {
}
