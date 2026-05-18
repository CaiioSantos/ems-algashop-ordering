package com.algaworks.algashop.ordering.infrastructure.adapters.in.listener.customer;

import com.algaworks.algashop.ordering.core.application.AbstractApplicationIT;
import com.algaworks.algashop.ordering.core.domain.model.commons.Email;
import com.algaworks.algashop.ordering.core.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.core.domain.model.order.OrderReadyEvent;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForAddingLoyaltypoints;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.algaworks.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers.NotifyNewRegistrationInput;

@TestPropertySource(properties = "spring.flyway.locations=classpath:db/migration,classpath:db/testdata")
class CustomerEventListenerTest extends AbstractApplicationIT {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoBean
    private ForNotifyingCustomers customerNotificationService;

    @MockitoBean
    private ForAddingLoyaltypoints customerLoyaltyPointsApplicationService;


    @Test
     void shouldListenOrderReadyEvent() {
        applicationEventPublisher.publishEvent(
                new OrderReadyEvent(
                        CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID,
                        new OrderId(),
                        OffsetDateTime.now()
                )
        );

        Mockito.verify(customerEventListener).listen(Mockito.any(OrderReadyEvent.class));

        Mockito.verify(customerLoyaltyPointsApplicationService).addLoyaltyPoints(
                Mockito.any(UUID.class),
                Mockito.any(String.class)
        );
    }

    @Test
     void shouldListenCustomerRegisteredEvent() {
        applicationEventPublisher.publishEvent(
                new CustomerRegisteredEvent(
                        CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID,
                        OffsetDateTime.now(),
                        new FullName("John", "Doe"),
                        new Email("john.doe@email.com")

                )
        );

        Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));
        Mockito.verify(customerNotificationService).notifyNewRegistration(
                Mockito.any(NotifyNewRegistrationInput.class));
    }
}