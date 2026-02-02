package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.application.order.query.OrderDetailOutput;
import com.algaworks.algashop.ordering.application.order.query.OrderQueryService;
import com.algaworks.algashop.ordering.application.utility.Mapper;
import com.algaworks.algashop.ordering.domain.order.OrderId;
import com.algaworks.algashop.ordering.domain.order.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderPersistenceEntityRepository repository;
    private final Mapper mapper;

    @Override
    public OrderDetailOutput findById(String id) {
        OrderPersistenceEntity orderPersistenceEntity = repository.findById(new OrderId(id).value().toLong())
                .orElseThrow(() -> new OrderNotFoundException());
        return mapper.convert(orderPersistenceEntity,OrderDetailOutput.class);
    }

}
