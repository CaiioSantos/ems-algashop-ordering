package com.algaworks.algashop.ordering.application.order.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInput {
    @Valid
    @NotNull
    private RecipientData recipient;
    @Valid
    @NotNull
    private AddressData address;
}