package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.algaworks.algashop.ordering.core.application.customer.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.core.application.customer.CustomerQueryService;
import com.algaworks.algashop.ordering.core.application.shoppingcart.ShoppingCartQueryService;
import com.algaworks.algashop.ordering.core.ports.in.customer.*;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ForQueryingShoppingCarts;
import com.algaworks.algashop.ordering.core.ports.in.shoppingcart.ShoppingCartOutput;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.PageModel;
import com.algaworks.algashop.ordering.infrastructure.config.security.SecurityAnnotations;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ForManagingCustomers forManagingCustomers;
    private final ForQueryingCustomers forQueryingCustomers;
    private final ForQueryingShoppingCarts forQueryingShoppingCarts;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityAnnotations.CanWriteCustomers
    public CustomerOutput create(@RequestBody @Valid CustomerInput input, HttpServletResponse httpServletResponse) {
        UUID customerId = forManagingCustomers.create(input);

        UriComponentsBuilder builder = fromMethodCall(on(CustomerController.class).findById(customerId));
        httpServletResponse.addHeader("Location", builder.toUriString());

        return forQueryingCustomers.findById(customerId);
    }

    @GetMapping
    @SecurityAnnotations.CanReadCustomers
    public PageModel<CustomerSummaryOutput> findAll(CustomerFilter customerFilter){
        return PageModel.of(forQueryingCustomers.filter(customerFilter));
    }

    @GetMapping("/{customerId}")
    @SecurityAnnotations.CanReadCustomers
    public  CustomerOutput findById(@PathVariable UUID customerId){
        return forQueryingCustomers.findById(customerId);
    }

    @GetMapping("/{customerId}/shopping-cart")
    @SecurityAnnotations.CanReadShoppingCarts
    public ShoppingCartOutput findShoppingCartCustomerId(@PathVariable UUID customerId){
        return forQueryingShoppingCarts.findByCustomerId(customerId);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    @SecurityAnnotations.CanWriteCustomers
    public  CustomerOutput update(@PathVariable UUID customerId, @RequestBody @Valid CustomerUpdateInput updateInput){
        forManagingCustomers.update(customerId,updateInput);
        return forQueryingCustomers.findById(customerId);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityAnnotations.CanWriteCustomers
    public  void delete(@PathVariable UUID customerId){
        forManagingCustomers.archive(customerId);
    }
}
