package com.algaworks.algashop.ordering.core.ports.in.commons;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressData {

    @NotBlank
    String street;

    String complement;

    @NotBlank
    String neighborhood;

    @NotBlank
    String number;

    @NotBlank
    String city;

    @NotBlank
    String state;

    @NotBlank
    String zipCode;
}
