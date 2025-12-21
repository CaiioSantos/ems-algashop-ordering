package com.algaworks.algashop.ordering.domain.repository;

import com.algaworks.algashop.ordering.domain.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {

//    @Query("""
//            from ShoppingCart
//            where customerId = :customerId
//            """)
    Optional<ShoppingCart> ofCustomer(@Param("customerId") CustomerId customerId);

}
