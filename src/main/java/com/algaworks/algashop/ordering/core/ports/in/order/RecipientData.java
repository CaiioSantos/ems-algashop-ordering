package com.algaworks.algashop.ordering.core.ports.in.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipientData {
    private String firstName;
    private String lastName;
    private String phone;
    private String document;
}
