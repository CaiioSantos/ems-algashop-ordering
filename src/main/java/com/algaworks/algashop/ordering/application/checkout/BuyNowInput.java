package com.algaworks.algashop.ordering.application.checkout;

import com.algaworks.algashop.ordering.application.order.query.BillingData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyNowInput {

    private ShippingInput shipping;

    @NotNull
    private BillingData billing;

    @NotNull
    private UUID productId;

    @NotNull
    private UUID customerId;

    @NotNull
    private Integer quantity;
    @NotBlank
    private String paymentMethod;
    private UUID creditCardId;

}
