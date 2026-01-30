package com.algaworks.algashop.ordering.domain.order;

import java.time.OffsetDateTime;

public record OrderPaidEvent(OrderId orderId, OffsetDateTime paidAt) {
}
