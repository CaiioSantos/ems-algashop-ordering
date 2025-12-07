package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {

    List<OrderPersistenceEntity> findByCustomer_IdAndPlacedAtBetween(
            UUID customerId,
            OffsetDateTime start,
            OffsetDateTime end
    );

    @Query("from OrderPersistenceEntity " +
            "where customer.id = :customerId " +
            "and YEAR(placedAt) =:year")
    List<OrderPersistenceEntity> placedByCustomerInYear(
            @Param("customerId") UUID customerId,
            @Param("year") Integer year
    );

    @Query("select count(*) from OrderPersistenceEntity " +
            "where customer.id = :customerId " +
            "and YEAR(placedAt) =:year " +
            "and paidAt IS NOT NULL " +
            "and canceledAt IS NULL "
    )
    long salesQuantityByCutomerInYear(
            @Param("customerId") UUID customerId,
            @Param("year") Integer year
    );

    @Query("select coalesce(sum(totalAmount), 0) from OrderPersistenceEntity " +
            "where customer.id = :customerId " +
            "and canceledAt IS NULL " +
            "and paidAt IS NOT NULL"
    )
    BigDecimal totalSoldForCustomer(
            @Param("customerId") UUID customerId);
}
