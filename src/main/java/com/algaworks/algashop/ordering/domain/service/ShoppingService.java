package com.algaworks.algashop.ordering.domain.service;

import com.algaworks.algashop.ordering.domain.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.exceptions.CustomerAlreadyHaveShoppingCartException;
import com.algaworks.algashop.ordering.domain.exceptions.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.repository.Customers;
import com.algaworks.algashop.ordering.domain.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.utility.DomainService;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {

    private final ShoppingCarts shoppingCarts;
    private final Customers customers;

    public ShoppingCart startShopping(CustomerId customerId){

        if (!customers.exists(customerId)) {
            throw new CustomerNotFoundException();
        }
        if (shoppingCarts.ofCustomer(customerId).isPresent()){
            throw new CustomerAlreadyHaveShoppingCartException();
        }
        ShoppingCart shoppingCart = ShoppingCart.startShopping(customerId);

        return shoppingCart;
    }
}
