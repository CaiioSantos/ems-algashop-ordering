package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.shipping;

import com.algaworks.algashop.ordering.core.application.shipping.ShippingApplicationService;
import com.algaworks.algashop.ordering.core.application.shipping.ShippingCostPreviewInput;
import com.algaworks.algashop.ordering.core.application.shipping.ShippingCostPreviewOutput;
import com.algaworks.algashop.ordering.infrastructure.config.security.SecurityAnnotations;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ShippingCostController {

    private final ShippingApplicationService shippingApplicationService;

    @PostMapping("api/v1/shipping-cost-previews")
    @SecurityAnnotations.CanPreviewShippingCosts
    public ShippingCostPreviewOutput previewShippingCost(@RequestBody @Valid ShippingCostPreviewInput input) {
        return shippingApplicationService.previewShippingCost(input);
    }
}
