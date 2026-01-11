package com.algaworks.algashop.ordering.domain.shoppingcart;

import com.algaworks.algashop.ordering.domain.RemoveCapableRepository;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {

//    @Query("""
//            from ShoppingCart
//            where customerId = :customerId
//            """)
    Optional<ShoppingCart> ofCustomer(@Param("customerId") CustomerId customerId);

}
