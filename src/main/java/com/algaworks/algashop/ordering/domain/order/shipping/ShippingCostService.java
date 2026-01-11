package com.algaworks.algashop.ordering.domain.order.shipping;

import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.commons.ZipCode;
import lombok.Builder;

import java.time.LocalDate;

public interface ShippingCostService {

    CalculationResult calculate(CalculationRequest request);

    @Builder
    record CalculationRequest(ZipCode origin, ZipCode destination) {}

    @Builder
    record CalculationResult(Money cost, LocalDate expectedDate) {}
}
