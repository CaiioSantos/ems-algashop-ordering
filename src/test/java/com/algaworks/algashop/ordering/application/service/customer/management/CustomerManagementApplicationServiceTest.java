package com.algaworks.algashop.ordering.application.service.customer.management;


import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.customer.management.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerUpdateInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceTest {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;


    @Test
    void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCostumer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);



        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
        Assertions.assertThat(customerOutput.getFirstName()).isEqualTo("John");
        Assertions.assertThat(customerOutput.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("johndoe@email.com");
        Assertions.assertThat(customerOutput.getBirthDate()).isEqualTo(LocalDate.of(1991, 7,5));
        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

    }

    @Test
    void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCostumer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCostumerUpdate().build();
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId,updateInput);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
        Assertions.assertThat(customerOutput.getFirstName()).isEqualTo("Matt");
        Assertions.assertThat(customerOutput.getLastName()).isEqualTo("Damon");
        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("johndoe@email.com");
        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

    }
}