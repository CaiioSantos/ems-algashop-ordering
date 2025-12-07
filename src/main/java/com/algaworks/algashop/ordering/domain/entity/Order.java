package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exceptions.*;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

public class Order implements AggregateRoot<OrderId> {

    private OrderId id;
    private CustomerId customerId;

    private Money totalAmount;
    private Quantity totalItens;

    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;

    private Billing billing;
    private Shipping shipping;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    private Set<OrderItem> items;

    private Long version;


    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
    public Order(OrderId id,Long version, CustomerId customerId, Money totalAmount,
                 Quantity totalItems, OffsetDateTime placedAt, OffsetDateTime paidAt,
                 OffsetDateTime canceledAt, OffsetDateTime readyAt, Billing billing,
                 Shipping shipping,OrderStatus status, PaymentMethod paymentMethod,
                 Set<OrderItem> items) {

        this.setId(id);
        this.setVersion(version);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItens(totalItems);
        this.setPlacedAt(placedAt);
        this.setPaidAt(paidAt);
        this.setCanceledAt(canceledAt);
        this.setReadyAt(readyAt);
        this.setBilling(billing);
        this.setShipping(shipping);
        this.setStatus(status);
        this.setPaymentMethod(paymentMethod);
        this.setItems(items);
    }


    public static Order draft(CustomerId customerId) {
        return new Order(
                new OrderId(),
                null,
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                null,
                null,
                null,
                null,
                null,
                null,
                OrderStatus.DRAFT,
                null,
                new HashSet<>()
        );
    }

    public void addItem(Product product,  Quantity quantity) {
        this.verifyIfChangeable();
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);


        product.checkOutOfStock();

        OrderItem orderItem = OrderItem.brandNew()
                    .orderId(this.id())
                    .quantity(quantity)
                    .product(product)
                    .build();

        if (this.items == null){
            this.items = new HashSet<>();
        }
        this.items.add(orderItem);
        this.recalculateTotal();
    }

    public void place(){
        this.verifyIfCanChangetoPlaced();
        this.changeStatus(OrderStatus.PLACED);
        this.setPlacedAt(OffsetDateTime.now());
    }

    public void markAsPaid() {
        this.changeStatus(OrderStatus.PAID);
        this.setPaidAt(OffsetDateTime.now());
    }

    public void markAsReady() {
        this.changeStatus(OrderStatus.READY);
        this.setReadyAt(OffsetDateTime.now());
    }

    public void cancel(){
        if (isCanceled()){
            throw new OrderStatusCannotBeChangedException(this.id(), this.status(), OrderStatus.CANCELED);
        }
        this.changeStatus(OrderStatus.CANCELED);
        this.setCanceledAt(OffsetDateTime.now());
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        this.verifyIfChangeable();
        Objects.requireNonNull(paymentMethod);
        this.setPaymentMethod(paymentMethod);
    }

    public void  changeBilling(Billing billing) {
        this.verifyIfChangeable();
        Objects.requireNonNull(billing);
        this.setBilling(billing);
    }

    public void changeShipping (Shipping newShipping) {
        this.verifyIfChangeable();
        Objects.requireNonNull(newShipping);
        if (newShipping.expectedDate().isBefore(LocalDate.now())) {
            throw new OrderInvalidShippingDeliveryDateException(this.id());
        }

        this.setShipping(newShipping);
        this.recalculateTotal();
    }

    public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
        this.verifyIfChangeable();
        Objects.requireNonNull(orderItemId);
        Objects.requireNonNull(quantity);
        OrderItem orderItem = this.findOrderItem(orderItemId);
        orderItem.changeQuantity(quantity);

        this.recalculateTotal();
    }

    public boolean isDrasft() {
        return OrderStatus.DRAFT.equals(this.status());
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(this.status());
    }
    public boolean isPaid() {
        return OrderStatus.PAID.equals(this.status());
    }

    public boolean isReady() {
        return OrderStatus.READY.equals(this.status());
    }

    public boolean isCanceled() {
        return OrderStatus.CANCELED.equals(this.status());
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItens() {
        return totalItens;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public Billing billing() {
        return billing;
    }

    public Shipping shipping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(this.items);
    }

    private void recalculateTotal() {
        BigDecimal totalItemsAmount = this.items().stream().map(total -> total.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItensQuantity = this.items().stream().map(total -> total.quantity().value())
                .reduce(0,Integer::sum);

        BigDecimal shippingCost;

        if (this.shipping() == null){
            shippingCost = BigDecimal.ZERO;
        }else {
            shippingCost = this.shipping.cost().value();
        }
        BigDecimal totalAmount = totalItemsAmount.add(shippingCost);

        this.setTotalAmount(new Money(totalAmount));
        this.setTotalItens(new Quantity(totalItensQuantity));
    }

    private void changeStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus);
        if (this.status().canNotChangeTo(newStatus)){
            throw new OrderStatusCannotBeChangedException(this.id(), this.status(), newStatus);
        }
        this.setStatus(newStatus);

    }

    private void verifyIfCanChangetoPlaced() {
        if (this.shipping() == null){
            throw  OrderCannotBePlacedException.noShippingInfo(this.id());
        }
        if (this.billing() == null){
            throw  OrderCannotBePlacedException.noBillingInfo(this.id());
        }
        if (this.paymentMethod() == null){
            throw  OrderCannotBePlacedException.noPaymentMethod(this.id());
        }
        if (this.items() == null || this.items().isEmpty()){
            throw  OrderCannotBePlacedException.noItens(this.id());
        }

    }

    private OrderItem findOrderItem(OrderItemId orderItemId) {
        Objects.requireNonNull(orderItemId);
        return this.items().stream()
                .filter(item -> item.id().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
    }
    private void verifyIfChangeable() {
        if(!isDrasft()){
            throw new OrderCannotBeEditedException(this.id(), this.status());
        }
    }

    public void removeItem(OrderItemId orderItemId){
        this.verifyIfChangeable();
        Objects.requireNonNull(orderItemId);
        OrderItem itemToRemove = this.findOrderItem(orderItemId);
        this.items.remove(itemToRemove);
        this.recalculateTotal();

    }


    private void setId(OrderId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public Long version(){
        return version;
    }

    private void setVersion(Long version) {
        this.version = version;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItens(Quantity totalItens) {
        Objects.requireNonNull(totalItens);
        this.totalItens = totalItens;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setBilling(Billing billing) {
        this.billing = billing;
    }

    private void setShipping(Shipping shipping) {
        this.shipping = shipping;

    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setItems(Set<OrderItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
