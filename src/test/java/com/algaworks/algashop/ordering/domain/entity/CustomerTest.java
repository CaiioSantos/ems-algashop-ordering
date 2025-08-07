package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exceptions.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class CustomerTest {

    @Test
    void testInvalidEmailCreateException(){

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> CustomertestDataBuilder.brandNewCustomer().email(new Email("invalid")).build());
    }

    @Test
    void testInvalidEmailUpdateException(){
        Customer customer = CustomertestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> {
                    customer.changeEmail("invalid");
                });
    }

    @Test
    void unarchivedCUstomerTest() {
        Customer customer = CustomertestDataBuilder.existingCustomer().build();

        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo(new FullName("Caio", "Santos")),
                c -> assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
                c -> assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
                c -> assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c -> assertThat(c.address()).isEqualTo(Address.builder()
                        .street("Bourbon Street")
                        .number("Anonymized")
                        .neighborhood("Noth Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement(null)
                        .build()));

    }

    @Test
    void unarchivedCustomerTestException(){
        Customer customer = CustomertestDataBuilder.existing().build();

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("email@teste.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("123-123-1111"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.enablePromotionNotifications());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.disablePromotionNotifications());
    }

    @Test
    void unArchivedNewCustomerAddloyalPointsTest() {
        Customer customer = CustomertestDataBuilder.brandNewCustomer().build();

        customer.addloyaltyPoints(new LoyaltyPoints(10));
        customer.addloyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPoints().value()).isEqualTo(new LoyaltyPoints(30).value());

    }

    @Test
    void unArchivedNewCustomerAddloyalPointsTestException() {
        Customer customer = CustomertestDataBuilder.brandNewCustomer().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addloyaltyPoints(new LoyaltyPoints(0)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addloyaltyPoints(new LoyaltyPoints(-1)));

    }

}