package com.algaworks.algashop.ordering.domain.product;

import java.util.Optional;

public interface ProductCatalogService {

    Optional<Product> ofId(ProductId productId);
}
