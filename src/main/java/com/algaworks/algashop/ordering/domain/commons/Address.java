package com.algaworks.algashop.ordering.domain.commons;

import com.algaworks.algashop.ordering.domain.FieldValidations;
import lombok.Builder;

import java.util.Objects;

public record Address(
        String street,
        String complement,
        String neighborhood,
        String number,
        String city,
        String state,
        ZipCode zipCode

) {
    @Builder(toBuilder = true)
    public Address{
        FieldValidations.requiresNonBlank(street);
        FieldValidations.requiresNonBlank(neighborhood);
        FieldValidations.requiresNonBlank(number);
        FieldValidations.requiresNonBlank(city);
        FieldValidations.requiresNonBlank(state);
        Objects.requireNonNull(zipCode);
    }
}
