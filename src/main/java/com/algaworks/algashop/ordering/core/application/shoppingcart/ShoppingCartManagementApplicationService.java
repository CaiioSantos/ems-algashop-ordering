package com.algaworks.algashop.ordering.core.application.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.product.Product;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.*;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppintCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartManagementApplicationService implements ForManagingShoppintCarts {

    private final ShoppingCarts shoppingCarts;
    private final ShoppingService shoppingService;
    private final ProductCatalogService productCatalogService;


    @Transactional
    @Override
    public UUID create(UUID customerId) {
        Objects.requireNonNull(customerId);
        ShoppingCart shoppingCart = shoppingService.startShopping(new CustomerId(customerId));
        shoppingCarts.add(shoppingCart);
        return shoppingCart.id().value();
    }
    @Transactional
    @Override
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
    @Override
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
    @Override
    public void empty(UUID shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(shoppingCartId))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
        shoppingCart.empty();
        shoppingCarts.add(shoppingCart);
    }

    @Transactional
    @Override
    public void delete(UUID shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(shoppingCartId))
                .orElseThrow(() -> new ShoppingCartNotFoundException());
        shoppingCarts.remove(shoppingCart.id());
    }

}
