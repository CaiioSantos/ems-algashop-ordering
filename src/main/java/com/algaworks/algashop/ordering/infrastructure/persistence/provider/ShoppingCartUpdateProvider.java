package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.service.ShoppingCartProductAdjustmentService;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateProvider implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository persistenceEntityRepository;

    @Override
    @Transactional
    public void adjustPrice(ProductId productId, Money updatePrice) {
    persistenceEntityRepository.updateItemPrice(productId.value(), updatePrice.value());
    persistenceEntityRepository.recalculateTotalsForCartsWitchProduct(productId.value());
    }

    @Override
    @Transactional
    public void changeAvailability(ProductId productId, boolean available) {
        persistenceEntityRepository.updateItemAvailability(productId.value(), available);
    }
}
