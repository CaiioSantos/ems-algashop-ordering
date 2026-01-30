package com.algaworks.algashop.ordering.domain.order;

import java.time.OffsetDateTime;

public record OrderPlacedEvent(OrderId orderId, OffsetDateTime placedAt) {
}
