package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.AbstractPersistenceIT;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

@TestPropertySource(properties = "spring.flyway.locations=classpath:db/migration,classpath:db/testdata")
public class ShoppingCartPersistenceEntityRepositoryIT extends AbstractPersistenceIT {

    private final ShoppingCartPersistenceEntityRepository persistenceEntityRepository;
    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    private CustomerPersistenceEntity customerPersistenceEntity;

    @Autowired
    public ShoppingCartPersistenceEntityRepositoryIT(ShoppingCartPersistenceEntityRepository persistenceEntityRepository, CustomerPersistenceEntityRepository customerPersistenceEntityRepository) {
        this.persistenceEntityRepository = persistenceEntityRepository;
        this.customerPersistenceEntityRepository = customerPersistenceEntityRepository;
    }

    public static final CustomerId CUSTOMER_ID = new CustomerId(
            UUID.fromString("3a4b5c6d-7e8f-9a0b-1c2d-3e4f5a6b7c8d"));

    @BeforeEach
    public void setup() {
        UUID customerId = CUSTOMER_ID.value();
        customerPersistenceEntity = customerPersistenceEntityRepository.getReferenceById(customerId);
    }

    @Test
    public void shouldPersist() {
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
                .customer(customerPersistenceEntity)
                .build();
        persistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(persistenceEntityRepository.existsById(entity.getId())).isTrue();
        ShoppingCartPersistenceEntity orderPersistenceEntity = persistenceEntityRepository.findById(entity.getId()).orElseThrow();

        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
    }

    @Test
    public void shouldCount() {
        long orderCount = persistenceEntityRepository.count();
        Assertions.assertThat(orderCount).isEqualTo(2L);
    }

    @Test
    public void shouldSerAuditingValues() {
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntityTestDataBuilder.existingShoppingCart()
                .customer(customerPersistenceEntity)
                .build();
        entity = persistenceEntityRepository.saveAndFlush(entity);

        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }
}
