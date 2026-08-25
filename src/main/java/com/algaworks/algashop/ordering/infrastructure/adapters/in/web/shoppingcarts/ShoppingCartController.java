package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.shoppingcarts;

import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForManagingShoppintCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartItemInput;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.infrastructure.config.exceptionhandler.UnprocessableEntityException;
import com.algaworks.algashop.ordering.infrastructure.config.security.SecurityAnnotations;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-carts")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ForManagingShoppintCarts forManagingShoppintCarts;
    private final ForQueryingShoppingCarts shoppingCartQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityAnnotations.CanWriteShoppingCarts
    public ShoppingCartOutput create(@RequestBody @Valid ShoppingCartInput input, HttpServletResponse httpServletResponse) {
        UUID customerId;
        try {
            customerId = forManagingShoppintCarts.create(input.getCustomerId());

        }catch (CustomerNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
        return shoppingCartQueryService.findById(customerId);
    }

    @GetMapping("/{shoppingCartId}")
    @SecurityAnnotations.CanReadShoppingCarts
    public  ShoppingCartOutput findById(@PathVariable UUID shoppingCartId){
        return shoppingCartQueryService.findById(shoppingCartId);
    }


    @GetMapping("/{shoppingCartId}/items")
    @SecurityAnnotations.CanReadShoppingCarts
    public ShoppingCartItemListModel getItems(@PathVariable UUID shoppingCartId) {
        var items = shoppingCartQueryService.findById(shoppingCartId).getItems();
        return new ShoppingCartItemListModel(items);
    }

    @DeleteMapping("/{shoppingCartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityAnnotations.CanWriteShoppingCarts
    public void delete(@PathVariable UUID shoppingCartId) {
        forManagingShoppintCarts.delete(shoppingCartId);
    }

    @DeleteMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityAnnotations.CanWriteShoppingCarts
    public void empty(@PathVariable UUID shoppingCartId) {
        forManagingShoppintCarts.empty(shoppingCartId);
    }

    @PostMapping("/{shoppingCartId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityAnnotations.CanWriteShoppingCarts
    public void addItem(@PathVariable UUID shoppingCartId,
                        @RequestBody @Valid ShoppingCartItemInput input) {
        input.setShoppingCartId(shoppingCartId);
        try {
            forManagingShoppintCarts.addItem(input);
        }catch (ProductNotFoundException e) {
            throw new UnprocessableEntityException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{shoppingCartId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityAnnotations.CanWriteShoppingCarts
    public void removeItem(@PathVariable UUID shoppingCartId,
                           @PathVariable UUID itemId) {
        forManagingShoppintCarts.removeItem(shoppingCartId, itemId);
    }
}
