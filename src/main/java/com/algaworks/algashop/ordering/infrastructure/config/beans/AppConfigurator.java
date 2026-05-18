package com.algaworks.algashop.ordering.infrastructure.config.beans;

import com.algaworks.algashop.ordering.core.application.checkout.BuyNowApplicationService;
import com.algaworks.algashop.ordering.core.application.order.BillingInputDisassembler;
import com.algaworks.algashop.ordering.core.application.order.ShippingInputDisassembler;
import com.algaworks.algashop.ordering.core.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.core.domain.model.order.BuyNowService;
import com.algaworks.algashop.ordering.core.domain.model.order.Orders;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import com.algaworks.algashop.ordering.core.domain.model.product.ProductCatalogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigurator {

    @Bean
    public BuyNowApplicationService buyNowApplicationService(BuyNowService buyNowService, ProductCatalogService productCatalogService,
                                                             ShippingCostService shippingCostService, OriginAddressService originAddressService,
                                                             Orders orders, Customers customers,
                                                             ShippingInputDisassembler shippingInputDisassembler,
                                                             BillingInputDisassembler billingInputDisassembler) {
        return new BuyNowApplicationService(
                buyNowService,
                productCatalogService,
                shippingCostService,
                originAddressService,
                orders,
                customers,
                shippingInputDisassembler,
                billingInputDisassembler);
    }
}
