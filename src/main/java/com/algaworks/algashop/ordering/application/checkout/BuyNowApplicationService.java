package com.algaworks.algashop.ordering.application.checkout;

import com.algaworks.algashop.ordering.domain.commons.Quantity;
import com.algaworks.algashop.ordering.domain.commons.ZipCode;
import com.algaworks.algashop.ordering.domain.customer.Customer;
import com.algaworks.algashop.ordering.domain.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.customer.Customers;
import com.algaworks.algashop.ordering.domain.order.*;
import com.algaworks.algashop.ordering.domain.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.product.ProductId;
import com.algaworks.algashop.ordering.domain.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BuyNowApplicationService {

    private final BuyNowService buyNowService;
    private final ProductCatalogService productCatalogService;

    private final ShippingCostService shippingCostService;
    private final OriginAddressService originAddressService;

    private final Orders orders;
    private final Customers customers;

    private final ShippingInputDisassembler shippingInputDisassembler;
    private final BillingInputDisassembler billingInputDisassembler;

    @Transactional
    public String buyNow(BuyNowInput input) {
        Objects.requireNonNull(input);

        PaymentMethod paymentMethod = PaymentMethod.valueOf(input.getPaymentMethod());
        CustomerId customerId = new CustomerId(input.getCustomerId());
        Quantity quantity = new Quantity(input.getQuantity());
        ProductId productId = new ProductId(input.getProductId());

        Customer customer = customers.ofId(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        Product product = productCatalogService.ofId(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        var shippingCalculationResult = this.calculateShippingCOst(input.getShipping());

        Shipping shipping = shippingInputDisassembler.toDomainModel(input.getShipping(), shippingCalculationResult);

        Billing billing = billingInputDisassembler.toDomainModel(input.getBilling());

        Order order = buyNowService.buyNow(product,customer,billing,shipping,quantity,paymentMethod);
        orders.add(order);
        return order.id().toString();

    }

    private ShippingCostService.CalculationResult calculateShippingCOst(ShippingInput shippingInput) {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode(shippingInput.getAddress().getZipCode());
        return shippingCostService.calculate(new ShippingCostService.CalculationRequest(
                origin,
                destination
        ));

    }

}
