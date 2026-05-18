package com.algaworks.algashop.ordering.core.application.customer;


import com.algaworks.algashop.ordering.core.ports.in.commons.AddressData;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerUpdateInput;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder aCostumerUpdate(){
        return CustomerUpdateInput.builder()
                .firstName("Matt")
                .lastName("Damon")
                .phone("123-256-7899")
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Parkway Street")
                        .number("1800")
                        .complement("")
                        .neighborhood("Mountain Ville")
                        .city("Mountain Ville")
                        .state("California")
                        .zipCode("98765")
                        .build());

    }
}
