package com.algaworks.algashop.ordering.core.application.service.customer.management;


import com.algaworks.algashop.ordering.core.application.AbstractApplicationIT;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerInput;
import com.algaworks.algashop.ordering.core.application.customer.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerUpdateInput;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerQueryService;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerArchivedEvent;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerArchivedException;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.listener.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDate;
import java.util.UUID;


class CustomerManagementApplicationServiceIT extends AbstractApplicationIT {


    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private ForNotifyingCustomers customerNotificationService;

    @Autowired
    private CustomerQueryService customerQueryService;


    @Test
    void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustumer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerQueryService.findById(customerId);



        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
        Assertions.assertThat(customerOutput.getFirstName()).isEqualTo("John");
        Assertions.assertThat(customerOutput.getLastName()).isEqualTo("Doe");
        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("johndoe@email.com");
        Assertions.assertThat(customerOutput.getBirthDate()).isEqualTo(LocalDate.of(1991, 7,5));
        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
        Mockito.verify(customerEventListener, Mockito.never())
                .listen(Mockito.any(CustomerArchivedEvent.class));

        Mockito.verify(customerNotificationService)
                .notifyNewRegistration(Mockito.any(ForNotifyingCustomers.NotifyNewRegistrationInput.class));

    }

    @Test
    void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustumer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCostumerUpdate().build();
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId,updateInput);

        CustomerOutput customerOutput = customerQueryService.findById(customerId);

        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
        Assertions.assertThat(customerOutput.getFirstName()).isEqualTo("Matt");
        Assertions.assertThat(customerOutput.getLastName()).isEqualTo("Damon");
        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("johndoe@email.com");
        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

    }

    @Test
    void shouldArchive() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustumer().build();
        UUID customerId = customerManagementApplicationService.create(input);


        customerManagementApplicationService.archive(customerId);

        CustomerOutput archivedCustomer = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(archivedCustomer)
                .isNotNull()
                .extracting(
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPromotionNotificationsAllowed
                ).containsExactly(
                        "Anonymous",
                        "Anonymous",
                        "000-000-0000",
                        "000-00-0000",
                        null,
                        false
                );

        Assertions.assertThat(archivedCustomer.getEmail()).endsWith("@anonymous.com");
        Assertions.assertThat(archivedCustomer.getArchived()).isTrue();
        Assertions.assertThat(archivedCustomer.getArchivedAt()).isNotNull();

        Assertions.assertThat(archivedCustomer.getAddress()).isNotNull();
        Assertions.assertThat(archivedCustomer.getAddress().getNumber()).isNotNull().isEqualTo("Anonymized");
        Assertions.assertThat(archivedCustomer.getAddress().getComplement()).isNull();
    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenArchivingNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(nonExistingId));
    }

    @Test
    public void shouldThrowCustomerArchivedExceptionWhenArchivingAlreadyArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustumer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }

    @Test
    void shouldChangeEmail() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustumer().build();
        UUID customerId = customerManagementApplicationService.create(input);

        customerManagementApplicationService.changeEmail(customerId,"teste@Email.com");

        CustomerOutput customerOutput = customerQueryService.findById(customerId);

        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("teste@Email.com");

    }
    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenChangeEmailNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(nonExistingId, "teste@Email.com"));
    }
    @Test
    public void shouldThrowCustomerArchivedExceptionWhenChangeEmailAlreadyArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustumer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "teste@Email.com"));
    }

}