package com.algaworks.algashop.ordering.application.checkout;

import com.algaworks.algashop.ordering.domain.DomainException;
import com.algaworks.algashop.ordering.domain.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.customer.Customers;
import com.algaworks.algashop.ordering.domain.order.*;
import com.algaworks.algashop.ordering.domain.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.domain.shoppingcart.ShoppingCarts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CheckoutApplicationService {

    private final ShoppingCarts shoppingCarts;
    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;
    private final Orders orders;
    private final Customers customers;

    private final CheckoutService checkoutService;

    private final BillingInputDisassembler billingInputDisassembler;
    private final ShippingInputDisassembler shippingInputDisassembler;

    @Transactional
    public String checkout(CheckoutInput input) {
        Objects.requireNonNull(input);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());
        CreditCardId creditCardId = null;

        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            if (input.getCreditCardId() == null) {
                throw new DomainException("Credit card id is required when payment method is credit card");
            }
            creditCardId = new CreditCardId(input.getCreditCardId());
        }

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
                .orElseThrow(() -> new ShoppingCartNotFoundException());


        ShippingCostService.CalculationResult result = shippingCostService.calculate(new ShippingCostService.CalculationRequest(
                originAddressService.originAddress().zipCode(),
                new ZipCode(input.getShipping().getAddress().getZipCode())));

        Customer customer = customers.ofId(shoppingCart.customerId()).orElseThrow(() -> new CustomerNotFoundException());

        Order order = checkoutService.checkout(
                customer,
                shoppingCart,
                billingInputDisassembler.toDomainModel(input.getBilling()),
                shippingInputDisassembler.toDomainModel(input.getShipping(), result),
                paymentMethod, creditCardId);

        orders.add(order);

        shoppingCarts.add(shoppingCart);

        return order.id().value().toString();
    }
}
