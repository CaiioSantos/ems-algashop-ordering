package com.algaworks.algashop.ordering.infrastructure.listener.customer;

import com.algaworks.algashop.ordering.application.customer.loyalpoints.CustomerLoyaltyPointsApplicationService;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.algaworks.algashop.ordering.domain.commons.Email;
import com.algaworks.algashop.ordering.domain.commons.FullName;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.domain.order.OrderId;
import com.algaworks.algashop.ordering.domain.order.OrderReadyEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationService.NotifyNewRegistrationInput;

@SpringBootTest
class CustomerEventListenerTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoBean
    private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @MockitoBean
    private CustomerNotificationService customerNotificationService;

    @Test
    public  void shouldListenOrderReadyEvent() {
        applicationEventPublisher.publishEvent(
                new OrderReadyEvent(
                        new CustomerId(),
                        new OrderId(),
                        OffsetDateTime.now()
                )
        );
        Mockito.verify(customerEventListener).listen(Mockito.any(OrderReadyEvent.class));
        Mockito.verify(customerLoyaltyPointsApplicationService).addLoyaltyPoints(
                Mockito.any(UUID.class),
                Mockito.any(String.class));

    }

    @Test
    public void shouldListenCustomerRegisteredEvent() {
        applicationEventPublisher.publishEvent(
                new CustomerRegisteredEvent(
                        new CustomerId(),
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