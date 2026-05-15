package com.algaworks.algashop.ordering.core.domain.model.order;

import java.time.OffsetDateTime;

public record OrderCanceledEvent(OrderId orderId, OffsetDateTime canceledAt) {
}
