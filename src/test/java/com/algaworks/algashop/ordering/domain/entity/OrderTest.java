package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exceptions.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.exceptions.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate() {
        Order order = Order.draft(new CustomerId());
    }

    @Test
    public void shouldAddItem() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltProcessor().build();

        order.addItem(product,new Quantity(1));

        Assertions.assertThat(order.items().size()).isEqualTo(1);

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) ->Assertions.assertThat(i.productName())
        );

        Assertions.assertWith(orderItem,
                (i) -> Assertions.assertThat(i.id()).isNotNull(),
                (i) -> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Core 7 ultra")),
                (i) -> Assertions.assertThat(i.price()).isEqualTo(new Money("2000.00")),
                (i) -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1))
        );

    }

    @Test
    public void shouldGenerateExceptionItemSet() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();

        order.addItem(product,new Quantity(1));

        Set<OrderItem> itens = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(itens::clear);
    }

    @Test
    public void shouldCalculateTotals() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltRamMemory().build();
        ProductId productId = new ProductId();

        order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(),new Quantity(2));
        order.addItem(ProductTestDataBuilder.aProductAltProcessor().build(),new Quantity(1)
        );

        Set<OrderItem> itens = order.items();

        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("2400"));
        Assertions.assertThat(order.totalItens()).isEqualTo(new Quantity(3));
    }

    @Test
    public void givenDraftOrder_whenPlace_shouldChangeToPlaced() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenMarkAsPaid_shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        order.markAsPaid();
        Assertions.assertThat(order.isPaid()).isTrue();
        Assertions.assertThat(order.paidAt()).isNotNull();
    }


    @Test
    public void givenDraftOrder_whenPlace_shouldChangeToGenerateException() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_shouldAllowChange() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        Assertions.assertWith(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenDraftOrder_whenChangeBillingInfo_shouldAllwChange() {
        Address address = Address.builder()
                .street("Fragoso")
                .number("255")
                .neighborhood("rua goiana")
                .city("Olinda")
                .state("Pernambuco")
                .zipCode(new ZipCode("53250"))
                .build();

        BillingInfo billingInfo =  BillingInfo.builder()
                .address(address)
                .document(new Document("111-222-333-55"))
                .phone(new Phone("99-66551-1445"))
                .fullName(new FullName("Jose", "Pardal"))
                .build();

        Order order = Order.draft(new CustomerId());
        order.changeBilling(billingInfo);

        BillingInfo billingInfo2 =  BillingInfo.builder()
                .address(address)
                .document(new Document("111-222-333-55"))
                .phone(new Phone("99-66551-1445"))
                .fullName(new FullName("Jose", "Pardal"))
                .build();

        Assertions.assertThat(order.billing()).isEqualTo(billingInfo2);
    }

    @Test
    public void givenDraftOerder_whenChangeShipping_shouldAllowChanged() {
        Shipping shippingInfo = OrderTestDataBuilder.aShipping();
        Order order = Order.draft(new CustomerId());




        order.changeShipping(shippingInfo);

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.shipping()).isEqualTo(shippingInfo));

    }

    @Test
    public void givenDraftOerderAndDeliveryDate_whenChangeShippingInfo_shouldNotAllowChanged() {
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(2);

        Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder()
                .expectedDate(expectedDeliveryDate)
                .build();

        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(shipping));

    }

    @Test
    public void givenDraftoOrder_whenChangeItem_sholdRecalculate(){
        Order order = Order.draft(new CustomerId());

        order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1)
        );

        OrderItem orderItem = order.items().iterator().next();

        order.changeItemQuantity(orderItem.id(), new Quantity(3));

        Assertions.assertWith(order,
                (o) -> Assertions.assertThat(o.totalAmount()).isEqualTo(new Money("600.00")),
                (o) -> Assertions.assertThat(o.totalItens()).isEqualTo(new Quantity(3))
        );
    }

    @Test
    public void givenOutOfStockProduct_whenTryToAddToAndOrder_shouldNotAllow() {
        Order order = Order.draft(new CustomerId());

        Assertions.assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> order.addItem(ProductTestDataBuilder.aProductUnavailable().build(),
                        new Quantity(1)));

    }
}