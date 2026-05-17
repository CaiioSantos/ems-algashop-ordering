package com.algaworks.algashop.ordering.core.ports.in.order;


import com.algaworks.algashop.ordering.core.ports.in.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingData {

    private String firstName;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private AddressData address;
}
