package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer;

import com.algaworks.algashop.ordering.core.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerFilter;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerSummaryOutput;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForObtainingCustomers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForObtainingCustomersEntityManagerImpl implements ForObtainingCustomers {

    private final EntityManager entityManager;

    private static final String findByIdAsOutputJPQL = """
            SELECT new com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput(
                c.id,
                c.firstName,
                c.lastName,
                c.email,
                c.document,
                c.phone,
                c.birthDate,
                c.loyaltyPoints,
                c.registeredAt,
                c.archivedAt,
                c.promotionNotificationsAllowed,
                c.archived,
                new com.algaworks.algashop.ordering.core.ports.in.commons.AddressData(
                    c.address.street,
                    c.address.number,
                    c.address.complement,
                    c.address.neighborhood,
                    c.address.city,
                    c.address.state,
                    c.address.zipCode
                )
            )
            FROM CustomerPersistenceEntity c
            WHERE c.id = :id""";

    @Override
    public CustomerOutput findById(UUID customerId) {
        try {
            TypedQuery<CustomerOutput> query = entityManager.createQuery(findByIdAsOutputJPQL, CustomerOutput.class);
            query.setParameter("id", customerId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new CustomerNotFoundException();
        }
    }

    public Page<CustomerSummaryOutput> filter(CustomerFilter pageFilter) {
        Long totalQueryResult = this.countTotalQueryResults(pageFilter);
        if (totalQueryResult.equals(0l)){
            PageRequest request = PageRequest.of(pageFilter.getPage(), pageFilter.getSize());
            return new PageImpl<>(new ArrayList<>(), request,totalQueryResult);
        }
        return this.filterQuery(pageFilter, totalQueryResult);
    }

    private Long countTotalQueryResults(CustomerFilter pageFilter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<CustomerPersistenceEntity> root = criteriaQuery.from(CustomerPersistenceEntity.class);

        Expression<Long> count = builder.count(root);
        Predicate[] predicates = toPredicates(builder, root, pageFilter);

        criteriaQuery.select(count);
        criteriaQuery.where(predicates);
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);

        return query.getSingleResult();
    }

    private PageImpl<CustomerSummaryOutput> filterQuery(CustomerFilter filter, Long totalQueryResult) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerSummaryOutput> criteriaQuery = builder.createQuery(CustomerSummaryOutput.class);
        Root<CustomerPersistenceEntity> root = criteriaQuery.from(CustomerPersistenceEntity.class);

        criteriaQuery.select(
                builder.construct(CustomerSummaryOutput.class,
                        root.get("id"),
                        root.get("firstName"),
                        root.get("lastName"),
                        root.get("email"),
                        root.get("phone"),
                        root.get("document"),
                        root.get("birthDate"),
                        root.get("loyaltyPoints"),
                        root.get("registeredAt"),
                        root.get("archivedAt"),
                        root.get("promotionNotificationsAllowed"),
                        root.get("archived")
                )
        );
        Predicate[] predicates = toPredicates(builder, root, filter);
        Order sortOrder = this.toSortCustomer(builder,root, filter);

        criteriaQuery.where(predicates);

        if (sortOrder != null) {
            criteriaQuery.orderBy(sortOrder);
        }
        TypedQuery<CustomerSummaryOutput> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(filter.getPage() * filter.getSize());
        typedQuery.setMaxResults(filter.getSize());

        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

        return new PageImpl<>(typedQuery.getResultList(), pageRequest, totalQueryResult);
    }

    private Order toSortCustomer(CriteriaBuilder builder, Root<CustomerPersistenceEntity> root, CustomerFilter pageFilter) {
        if (pageFilter.getSortDirectionOrDefault() == Sort.Direction.ASC){
            return builder.asc(root.get(pageFilter.getSortByPropertyOrDefault().getPropertyName()));
        }
        if (pageFilter.getSortDirectionOrDefault() == Sort.Direction.DESC){
            return builder.desc(root.get(pageFilter.getSortByPropertyOrDefault().getPropertyName()));
        }
        return null;
    }

    private Predicate[] toPredicates(CriteriaBuilder builder, Root<CustomerPersistenceEntity> root, CustomerFilter filter) {

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("firstName")), "%" + filter.getFirstName().toLowerCase() + "%"));
        }

        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
