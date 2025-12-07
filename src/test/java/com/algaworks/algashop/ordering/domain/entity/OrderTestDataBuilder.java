package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;

import java.time.LocalDate;

import static com.algaworks.algashop.ordering.domain.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class OrderTestDataBuilder {

    private CustomerId customerId = DEFAULT_CUSTOMER_ID;
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Shipping shipping = aShipping();
    private Billing billing = aBilling();

    private boolean withItems = true;

    private OrderStatus status = OrderStatus.DRAFT;



    private OrderTestDataBuilder() {

    }

    public static OrderTestDataBuilder anOrder() {
        return new OrderTestDataBuilder();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);

        if (withItems) {
            order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));
            order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        }

        switch (this.status) {
            case DRAFT -> {

            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
                order.place();
                order.markAsPaid();
                order.markAsReady();
            }
            case CANCELED -> {
            }
        }

        return order;
    }

    public static Shipping aShipping() {
        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .address(anAddress())
                .recipient(Recipient.builder()
                        .fullName(new FullName("Jose", "Pardal"))
                        .document(new Document("111-222-333-55"))
                        .phone(new Phone("99-66551-1445"))
                        .build())
                .build();
    }

    public static Billing aBilling() {
        return  Billing.builder()
                .address(anAddress())
                .document(new Document("111-222-333-55"))
                .phone(new Phone("99-66551-1445"))
                .fullName(new FullName("Jose", "Pardal"))
                .email(new Email("jhon.doe@gmail.com"))
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Fragoso")
                .number("255")
                .neighborhood("rua goiana")
                .city("Olinda")
                .state("Pernambuco")
                .zipCode(new ZipCode("53250"))
                .build();
    }

    public static Shipping aShippingAlt() {
        return Shipping.builder()
                .cost(new Money("20.00"))
                .expectedDate(LocalDate.now().plusWeeks(2))
                .address(anAddressAlt())
                .recipient(Recipient.builder()
                        .fullName(new FullName("Mary", "Jones"))
                        .document(new Document("552-11-4333"))
                        .phone(new Phone("54-454-1144"))
                        .build())
                .build();
    }

    public static Address anAddressAlt() {
        return Address.builder()
                .street("Sansome Street")
                .number("875")
                .neighborhood("Sansome")
                .city("San Francisco")
                .state("California")
                .zipCode(new ZipCode("08040"))
                .build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }


}
