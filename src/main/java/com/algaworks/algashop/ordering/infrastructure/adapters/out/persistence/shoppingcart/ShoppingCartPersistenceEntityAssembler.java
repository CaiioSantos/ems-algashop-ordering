package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;


import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.core.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartItemPersistenceEntity fromDomain(ShoppingCartItem item) {
        return mergeItem(new ShoppingCartItemPersistenceEntity(), item);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity persistenceEntity,
                                               ShoppingCart shoppingCart) {
        persistenceEntity.setId(shoppingCart.id().value());
        persistenceEntity.setCustomer(customerPersistenceEntityRepository.getReferenceById(shoppingCart.customerId().value()));
        persistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        persistenceEntity.setTotalItems(shoppingCart.totalItens().value());
        persistenceEntity.setCreatedAt(shoppingCart.createAt());
        Set<ShoppingCartItemPersistenceEntity> mergeItems = mergeItems(shoppingCart, persistenceEntity);
        syncItems(persistenceEntity, mergeItems);
        persistenceEntity.addEvents(shoppingCart.domainEvents());
        return persistenceEntity;
    }
    private void syncItems(ShoppingCartPersistenceEntity entity,
                           Set<ShoppingCartItemPersistenceEntity> updatedItems) {

        Set<ShoppingCartItemPersistenceEntity> currentItems = entity.getItems();

        // Remover os que não existem mais
        currentItems.removeIf(existing ->
                updatedItems.stream().noneMatch(u -> u.getId().equals(existing.getId()))
        );

        // Atualizar ou adicionar
        for (ShoppingCartItemPersistenceEntity updated : updatedItems) {

            Optional<ShoppingCartItemPersistenceEntity> existingOpt =
                    currentItems.stream()
                            .filter(i -> i.getId().equals(updated.getId()))
                            .findFirst();

            if (existingOpt.isPresent()) {
                // já atualizado no mergeItem (mesma instância)
                continue;
            }

            updated.setShoppingCart(entity);
            currentItems.add(updated);
        }
    }

    private Set<ShoppingCartItemPersistenceEntity> mergeItems(ShoppingCart shoppingCart, ShoppingCartPersistenceEntity shoppingCartPersistenceEntity){
        Set<ShoppingCartItem> newOrUpdateItems = shoppingCart.items();
        if (newOrUpdateItems == null || shoppingCart.items().isEmpty()){
            return new HashSet<>();
        }

        Set<ShoppingCartItemPersistenceEntity> existingItems = shoppingCartPersistenceEntity.getItems();

        if(existingItems == null || existingItems.isEmpty()){
            return newOrUpdateItems.stream().map(this::fromDomain).collect(Collectors.toSet());
        }

        Map<UUID, ShoppingCartItemPersistenceEntity> existingMap = existingItems.stream()
                .collect(Collectors.toMap(ShoppingCartItemPersistenceEntity::getId, item -> item));

        return newOrUpdateItems.stream()
                .map(shoppingCartItem -> {
                    ShoppingCartItemPersistenceEntity itemPersistence = existingMap.getOrDefault(
                            shoppingCartItem.id().value(), new ShoppingCartItemPersistenceEntity()
                    );
                    return mergeItem(itemPersistence, shoppingCartItem);
                }).collect(Collectors.toSet());
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity persistenceEntity, ShoppingCartItem shoppingCartItem
    ) {
        persistenceEntity.setId(shoppingCartItem.id().value());
        persistenceEntity.setProductId(shoppingCartItem.productId().value());
        persistenceEntity.setName(shoppingCartItem.name().value());
        persistenceEntity.setPrice(shoppingCartItem.price().value());
        persistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        persistenceEntity.setAvailable(shoppingCartItem.isAvailable());
        persistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
        return persistenceEntity;
    }

    private ShoppingCartItemPersistenceEntity toOrderItemsEntities(ShoppingCartItem source) {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(source.id().value())
                .shoppingCart(ShoppingCartPersistenceEntity.builder().id(source.shoppingCartId().value()).build())
                .productId(source.productId().value())
                .name(source.name().value())
                .price(source.price().value())
                .quantity(source.quantity().value())
                .available(source.isAvailable())
                .totalAmount(source.totalAmount().value())
                .build();
    }
}