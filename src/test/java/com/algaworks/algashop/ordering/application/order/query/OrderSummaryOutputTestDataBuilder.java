package com.algaworks.algashop.ordering.application.order.query;

import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.order.OrderId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderSummaryOutputTestDataBuilder {

    public static OrderSummaryOutput.OrderSummaryOutputBuilder placedOrder() {
        return OrderSummaryOutput.builder()
                .id(new OrderId().toString())
                .customer(CustomerMinimalOutput.builder()
                        .id(new CustomerId().value())
                        .firstName("John")
                        .lastName("Doe")
                        .document("12345")
                        .email("johndoe@email.com")
                        .phone("1191234564")
                        .build())
                .totalItems(2)
                .totalAmount(new BigDecimal("39.98"))
                .placedAt(OffsetDateTime.now())
                .paidAt(null)
                .canceledAt(null)
                .readyAt(null)
                .status("PLACED")
                .paymentMethod("GATEWAY_BALANCE");
    }

}