package com.algaworks.algashop.ordering.infrastructure.fake;

import com.algaworks.algashop.ordering.domain.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.commons.Address;
import com.algaworks.algashop.ordering.domain.commons.ZipCode;
import org.springframework.stereotype.Component;

@Component
public class FixedOriginAddressService implements OriginAddressService {

    @Override
    public Address originAddress() {
        return Address.builder()
                .street("Bourbon Street")
                .number("1234")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .build();
    }
}
