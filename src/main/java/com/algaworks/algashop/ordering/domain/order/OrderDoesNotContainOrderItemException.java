package com.algaworks.algashop.ordering.domain.order;

import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.ErrorMessages;

public class OrderDoesNotContainOrderItemException extends DomainException {

    public OrderDoesNotContainOrderItemException(OrderId id, OrderItemId orderItemId) {
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM, id, orderItemId));
    }

}
