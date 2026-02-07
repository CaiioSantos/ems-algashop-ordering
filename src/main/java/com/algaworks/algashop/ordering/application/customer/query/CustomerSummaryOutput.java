package com.algaworks.algashop.ordering.application.customer.query;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSummaryOutput {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String document;
    private LocalDate birthDate;
    private Integer loyaltyPoints;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;

}
