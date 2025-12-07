package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.entity.Order;
import com.algaworks.algashop.ordering.domain.repository.Orders;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdersPersistenceProvider implements Orders {

    private final OrderPersistenceEntityRepository persistenceEntityRepository;
    private final OrderPersistenceEntityAssembler assembler;
    private final OrderPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Order>ofId(OrderId orderId) {
        Optional<OrderPersistenceEntity> possibleEntity = persistenceEntityRepository.findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Order aggregateRoot) {
        long orderId = aggregateRoot.id().value().toLong();
        persistenceEntityRepository.findById(orderId).ifPresentOrElse(
                (persistenceEntity) -> {
                    update(aggregateRoot,persistenceEntity);
                },
                () -> {
                    insert(aggregateRoot);
                }
        );

    }

    @Override
    public boolean exists(OrderId orderId) {
        return persistenceEntityRepository.existsById(orderId.value().toLong());
    }

    @Override
    public long count() {
        return persistenceEntityRepository.count();
    }

    @Override
    public List<Order> placedByCustomerInYear(CustomerId customerId, Year year) {
        OffsetDateTime start = year.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = start.plusYears(1).minusNanos(1);
        List<OrderPersistenceEntity> entityList = persistenceEntityRepository.placedByCustomerInYear(
                customerId.value(),
                year.getValue()
        );
        return entityList.stream().map(entity -> disassembler.toDomainEntity(entity)).collect(Collectors.toList());
    }

    @Override
    public long salesQuantityByCustomerInYear(CustomerId customerId, Year year) {
        return this.persistenceEntityRepository.salesQuantityByCutomerInYear(customerId.value(), year.getValue());
    }

    @Override
    public Money totalSoldForCustomer(CustomerId customerId) {
        return new Money(this.persistenceEntityRepository.totalSoldForCustomer(customerId.value()));
    }

    private void update(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = persistenceEntityRepository.saveAndFlush(persistenceEntity);

        this.updateVersion(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Order aggregateRoot, OrderPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);

    }

    private void insert(Order aggregateRoot) {
    OrderPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
    persistenceEntityRepository.saveAndFlush(persistenceEntity);

    }


}
