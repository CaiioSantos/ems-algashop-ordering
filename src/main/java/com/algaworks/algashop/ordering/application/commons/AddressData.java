package com.algaworks.algashop.ordering.application.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressData {

    String street;
    String complement;
    String neighborhood;
    String number;
    String city;
    String state;
    String zipCode;
}
