package com.algaworks.algashop.ordering.domain.service;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ZipCode;
import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public interface ShippingCostService {

    CalculationResult calculate(CalculationRequest request);

    @Builder
    record CalculationRequest(ZipCode origin, ZipCode destination) {}

    @Builder
    record CalculationResult(Money cost, LocalDate expectedDate) {}
}
