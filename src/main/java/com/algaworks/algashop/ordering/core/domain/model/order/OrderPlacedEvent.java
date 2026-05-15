package com.algaworks.algashop.ordering.core.domain.model.order;

import java.time.OffsetDateTime;

public record OrderPlacedEvent(OrderId orderId, OffsetDateTime placedAt) {
}
