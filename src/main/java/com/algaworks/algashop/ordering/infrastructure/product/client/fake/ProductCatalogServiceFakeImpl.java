package com.algaworks.algashop.ordering.infrastructure.product.client.fake;

import com.algaworks.algashop.ordering.domain.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.commons.Money;
import com.algaworks.algashop.ordering.domain.product.Product;
import com.algaworks.algashop.ordering.domain.product.ProductName;
import com.algaworks.algashop.ordering.domain.product.ProductId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCatalogServiceFakeImpl implements ProductCatalogService {
    @Override
    public Optional<Product> ofId(ProductId productId) {
        Product product = Product.builder()
                .id(productId)
                .inStock(true)
                .name(new ProductName("Notebook"))
                .price(new Money("3000"))
                .build();
        return Optional.of(product);
    }
}
