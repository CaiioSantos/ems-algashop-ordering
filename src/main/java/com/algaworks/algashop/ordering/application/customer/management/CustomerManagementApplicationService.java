package com.algaworks.algashop.ordering.application.customer.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.utility.Mapper;
import com.algaworks.algashop.ordering.domain.commons.*;
import com.algaworks.algashop.ordering.domain.customer.*;
import com.algaworks.algashop.ordering.domain.order.Order;
import com.algaworks.algashop.ordering.domain.order.OrderId;
import com.algaworks.algashop.ordering.domain.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

    private final CustomerRegistrationService customerRegistrationService;
    private final Customers customers;

    private final Mapper mapper;

    @Transactional
    public UUID create(CustomerInput input) {
        Objects.requireNonNull(input);
        AddressData address = input.getAddress();

        Customer customer = customerRegistrationService.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new BirthDate(input.getBirthDate()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                new Document(input.getDocument()),
                input.getPromotionNotificationsAllowed(),
                Address.builder()
                        .zipCode(new ZipCode(address.getZipCode()))
                        .state(address.getState())
                        .city(address.getCity())
                        .neighborhood(address.getNeighborhood())
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .complement(address.getComplement())
                        .build()
        );
        customers.add(customer);
        return customer.id().value();
    }

    @Transactional(readOnly = true)
    public CustomerOutput findById(UUID customerId) {
        Objects.requireNonNull(customerId);
        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        return mapper.convert(customer, CustomerOutput.class);
    }

    @Transactional(readOnly = true)
    public void update(UUID customerId, CustomerUpdateInput input) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(input);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changePhone(new Phone(input.getPhone()));

        if (input.getPromotionNotificationsAllowed().equals(Boolean.TRUE)){
            customer.enablePromotionNotifications();
        }else {
            customer.disablePromotionNotifications();
        }

        AddressData address = input.getAddress();

        customer.changeAdress(Address.builder()
                        .zipCode(new ZipCode(address.getZipCode()))
                        .state(address.getState())
                        .city(address.getCity())
                        .neighborhood(address.getNeighborhood())
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .complement(address.getComplement())
                        .build());

        customers.add(customer);
    }

    @Transactional
    public void archive(UUID customerId) {
        Objects.requireNonNull(customerId);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        if (Boolean.TRUE.equals(customer.isArchived()) && customer.archivedAt() != null) {
            throw new CustomerArchivedException();
        }

        customer.archive();
        customers.add(customer);


    }

    @Transactional
    public void changeEmail(UUID customerId, String newEmail) {
        Objects.requireNonNull(customerId);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        customerRegistrationService.changeEmail(customer, new Email(newEmail));
        customers.add(customer);


    }

}

