package com.algaworks.algashop.ordering.core.application.service.customer.management;

import com.algaworks.algashop.ordering.core.application.commons.AddressData;
import com.algaworks.algashop.ordering.core.application.customer.management.CustomerInput;

import java.time.LocalDate;

public class CustomerInputTestDataBuilder {

    public static CustomerInput.CustomerInputBuilder aCustumer(){
        return CustomerInput.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1991, 7,5))
                .document("255-08-0578")
                .phone("478-256-2604")
                .email("johndoe@email.com")
                .promotionNotificationsAllowed(false)
                .address(AddressData.builder()
                        .street("Bourbon Street")
                        .number("1200")
                        .complement("Apt. 114")
                        .neighborhood("North Ville")
                        .city("Yost fort")
                        .state("South California")
                        .zipCode("12345")
                        .build());

    }
}
