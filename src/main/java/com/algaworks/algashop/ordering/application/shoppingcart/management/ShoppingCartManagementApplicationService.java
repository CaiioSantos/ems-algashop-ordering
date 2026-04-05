package com.algaworks.algashop.ordering.application.shoppingcart.management;

import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.product.ProductId;
import com.algaworks.algashop.ordering.domain.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.domain.shoppingcart.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ShoppingService shoppingService;
    private final ProductCatalogService productCatalogService;


    @Transactional
    public UUID create(UUID customerId) {
        Objects.requireNonNull(customerId);
        ShoppingCart shoppingCart = shoppingService.startShopping(new CustomerId(customerId));
        shoppingCarts.add(shoppingCart);
        return shoppingCart.id().value();
    }
    @Transactional
    public void addItem(ShoppingCartItemInput input) {
        Objects.requireNonNull(input);
        ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
        ProductId productId = new ProductId(input.getProductId());

        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(()-> new ShoppingCartNotFoundException());

        Product product = productCatalogService.ofId(productId)
                .orElseThrow(()-> new ProductNotFoundException(productId));

        shoppingCart.addItem(product,new Quantity(input.getQuantity()));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
        Objects.requireNonNull(rawShoppingCartId);
        Objects.requireNonNull(rawShoppingCartItemId);
        ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
        ShoppingCart shoppingCart = shoppingCarts.ofId(shoppingCartId)
                .orElseThrow(()-> new ShoppingCartNotFoundException());
        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void empty(UUID shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(shoppingCartId))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
        shoppingCart.empty();
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    public void delete(UUID shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(shoppingCartId))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
        shoppingCarts.remove(shoppingCart.id());
    }

}
