package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.application.order.query.*;
import com.algaworks.algashop.ordering.application.utility.Mapper;
import com.algaworks.algashop.ordering.application.utility.PageFilter;
import com.algaworks.algashop.ordering.domain.order.OrderId;
import com.algaworks.algashop.ordering.domain.order.OrderNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceEntityRepository repository;
    private final Mapper mapper;

    private final EntityManager entityManager;

    @Override
    public OrderDetailOutput findById(String id) {
        OrderPersistenceEntity orderPersistenceEntity = repository.findById(new OrderId(id).value().toLong())
                .orElseThrow(() -> new OrderNotFoundException());
        return mapper.convert(orderPersistenceEntity,OrderDetailOutput.class);
    }

    @Override
    public Page<OrderSummaryOutput> filter(OrderFilter pageFilter) {
        Long totalQueryResult = this.countTotalQueryResults(pageFilter);
        if (totalQueryResult.equals(0l)){
            PageRequest request = PageRequest.of(pageFilter.getPage(), pageFilter.getSize());
            return new PageImpl<>(new ArrayList<>(), request,totalQueryResult);
        }
        return this.filterQuery(pageFilter, totalQueryResult);
    }

    private Long countTotalQueryResults(OrderFilter pageFilter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<OrderPersistenceEntity> root = criteriaQuery.from(OrderPersistenceEntity.class);

        Expression<Long> count = builder.count(root);
        Predicate[] predicates = toPredicates(builder, root, pageFilter);

        criteriaQuery.select(count);
        criteriaQuery.where(predicates);
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    private Page<OrderSummaryOutput> filterQuery(OrderFilter pageFilter, Long totalQueryResult) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderSummaryOutput> criteriaQuery = builder.createQuery(OrderSummaryOutput.class);
        Root<OrderPersistenceEntity> root = criteriaQuery.from(OrderPersistenceEntity.class);

        Path<Object> customer = root.get("customer");

        criteriaQuery.select(
                builder.construct(OrderSummaryOutput.class,
                        root.get("id"),
                        builder.construct(CustomerMinimalOutput.class,
                                customer.get("id"),
                                customer.get("firstName"),
                                customer.get("lastName"),
                                customer.get("email"),
                                customer.get("document"),
                                customer.get("phone")
                        ),
                        root.get("totaItems"),
                        root.get("totalAmount"),
                        root.get("placedAt"),
                        root.get("paidAt"),
                        root.get("canceledAt"),
                        root.get("readyAt"),
                        root.get("status"),
                        root.get("paymentMethod")
                        )
        );

        Predicate[] predicates = toPredicates(builder, root, pageFilter);
        criteriaQuery.where(predicates);

        TypedQuery<OrderSummaryOutput> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageFilter.getPage() * pageFilter.getSize());
        typedQuery.setMaxResults(pageFilter.getSize());

        PageRequest pageRequest = PageRequest.of(pageFilter.getPage(), pageFilter.getSize());

        return new PageImpl<>(typedQuery.getResultList(),pageRequest,totalQueryResult);
    }

    private Predicate[] toPredicates(CriteriaBuilder builder, Root<OrderPersistenceEntity> root, OrderFilter filter) {

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getCustomerId() != null) {
            predicates.add(builder.equal(root.get("customer").get("id"), filter.getCustomerId()));
        }

        return predicates.toArray(new Predicate[]{});
    }

}
