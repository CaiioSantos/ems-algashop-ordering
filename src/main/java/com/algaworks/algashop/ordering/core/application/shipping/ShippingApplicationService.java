package com.algaworks.algashop.ordering.core.application.shipping;

import com.algaworks.algashop.ordering.core.domain.model.commons.ZipCode;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.OriginAddressService;
import com.algaworks.algashop.ordering.core.domain.model.order.shipping.ShippingCostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShippingApplicationService {

    private final OriginAddressService originAddressService;
    private final ShippingCostService shippingCostService;

    public ShippingCostPreviewOutput previewShippingCost(ShippingCostPreviewInput input) {
        var originAddress = originAddressService.originAddress();
        ShippingCostService.CalculationRequest request = ShippingCostService.CalculationRequest.builder()
                .origin(originAddress.zipCode())
                .destination(new ZipCode(input.getZipCode()))
                .build();
        var shippingCost = shippingCostService.calculate(request);

        return new ShippingCostPreviewOutput(shippingCost.cost().value(), shippingCost.expectedDate());
    }
}
