package com.algaworks.algashop.ordering.core.domain.model.order;

import java.time.OffsetDateTime;

public record OrderPaidEvent(OrderId orderId, OffsetDateTime paidAt) {
}
