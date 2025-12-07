package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exceptions.ShoppingCartDoesNotContainItemException;
import com.algaworks.algashop.ordering.domain.exceptions.ShoppingCartDoesNotContainProductException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {

    private ShoppingCartId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItens;
    private OffsetDateTime createAt;

    private Set<ShoppingCartItem> items;


    @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existing")
    public ShoppingCart(ShoppingCartId id, CustomerId customerId, Money totalAmount,
                 Quantity totalItems, OffsetDateTime createAt,Set<ShoppingCartItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItens(totalItems);
        this.setCreateAt(createAt);
        this.setItems(items);
    }
    public static ShoppingCart startShopping(CustomerId customerId){
        return new ShoppingCart(new ShoppingCartId(), customerId, Money.ZERO, Quantity.ZERO, OffsetDateTime.now(), new HashSet<>());
    }

    public void empty(){
        totalAmount = Money.ZERO;
        totalItens = Quantity.ZERO;
        items.clear();
    }
    public void removeItem(ShoppingCartItemId cartItemId){
        ShoppingCartItem shoppingCartItem = this.findItem(cartItemId);
        this.items.remove(shoppingCartItem);
        this.recalculateTotals();
    }
    public void addItem(Product product, Quantity quantity){
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);
        product.checkOutOfStock();

        ShoppingCartItem shoppingCartItem = ShoppingCartItem.brandNew()
                .shoppingCartId(this.id())
                .productId(product.id())
                .productName(product.name())
                .price(product.price())
                .available(product.inStock())
                .quantity(quantity)
                .build();

        this.searchItemByProduct(product.id())
                .ifPresentOrElse(i -> updateItem(i, product, quantity), () -> insertItem(shoppingCartItem));


        this.recalculateTotals();
    }
    public void refreshItem(Product product) {
        ShoppingCartItem shoppingCartItem = this.findItem(product.id());
        shoppingCartItem.refresh(product);
        this.recalculateTotals();
    }
    public void changeItemQuantity(ShoppingCartItemId shoppingCartItemId, Quantity quantity) {
        ShoppingCartItem shoppingCartItem = this.findItem(shoppingCartItemId);
        shoppingCartItem.changeQuantity(quantity);
        this.recalculateTotals();
    }
    public ShoppingCartItem findItem(ShoppingCartItemId shoppingCartItemId) {
        Objects.requireNonNull(shoppingCartItemId);
        return this.items.stream()
                .filter(i -> i.id().equals(shoppingCartItemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id(), shoppingCartItemId));
    }

    public ShoppingCartItem findItem(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainProductException(this.id(), productId));
    }
    public void recalculateTotals(){
        BigDecimal totalAmount = items.stream()
                .map(i -> i.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = items.stream()
                .map(i -> i.quantity().value())
                .reduce(0, Integer::sum);

        this.totalAmount = new Money(totalAmount);
        this.totalItens = new Quantity(totalItems);

    }
    public boolean containsUnavailableItems() {
        return items.stream().anyMatch(i -> !i.isAvailable());
    }
    public boolean isEmpty(){
    return totalItens.equals(Quantity.ZERO);
    }

    public ShoppingCartId id() {
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

    public OffsetDateTime createAt() {
        return createAt;
    }

    public Set<ShoppingCartItem > items() {
        return items;
    }

    private void updateItem(ShoppingCartItem shoppingCartItem, Product product, Quantity quantity) {
        shoppingCartItem.refresh(product);
        shoppingCartItem.changeQuantity(shoppingCartItem.quantity().add(quantity));
    }

    private void insertItem(ShoppingCartItem shoppingCartItem) {
        this.items.add(shoppingCartItem);
    }

    private Optional<ShoppingCartItem> searchItemByProduct(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst();
    }

    public void setId(ShoppingCartId id) {
        this.id = id;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalItens(Quantity totalItens) {
        this.totalItens = totalItens;
    }

    public void setCreateAt(OffsetDateTime createAt) {
        this.createAt = createAt;
    }

    public void setItems(Set<ShoppingCartItem> items) {
        this.items = items;
    }
}
