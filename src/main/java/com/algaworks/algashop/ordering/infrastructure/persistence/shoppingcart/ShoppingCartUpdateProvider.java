package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartProductAdjustmentService;
import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.product.ProductId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateProvider implements ShoppingCartProductAdjustmentService {

    private final ShoppingCartPersistenceEntityRepository persistenceEntityRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void adjustPrice(ProductId productId, Money updatePrice) {
    persistenceEntityRepository.updateItemPrice(productId.value(), updatePrice.value());
    persistenceEntityRepository.recalculateTotalsForCartsWitchProduct(productId.value());
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    @Transactional
    public void changeAvailability(ProductId productId, boolean available) {
        persistenceEntityRepository.updateItemAvailability(productId.value(), available);
        entityManager.flush();
        entityManager.clear();
    }
}
