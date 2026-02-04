package com.algaworks.algashop.ordering.application.order.query;

import com.algaworks.algashop.ordering.application.commons.AddressData;
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
