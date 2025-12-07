package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.repository.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository persistenceEntityRepository;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        Optional<ShoppingCartPersistenceEntity> possibleEntity = persistenceEntityRepository.findByCustomer_Id(customerId.value());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public void remove(ShoppingCart shoppingCart) {

    }

    @Override
    public void remove(ShoppingCartId shoppingCartId) {

    }

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        return Optional.empty();
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return false;
    }

    @Override
    public void add(ShoppingCart aggregateRoot) {

    }

    @Override
    public long count() {
        return 0;
    }
}
