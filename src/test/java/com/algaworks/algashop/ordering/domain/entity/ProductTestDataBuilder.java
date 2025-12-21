package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;

public class ProductTestDataBuilder {

    public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();
    public ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .id(DEFAULT_PRODUCT_ID)
                .inStock(true)
                .name(new ProductName("MacBook"))
                .price(new Money("3000"));
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
                .id(new ProductId())
                .inStock(false)
                .name(new ProductName("GalaxyBook"))
                .price(new Money("5000"));
    }

    public static Product.ProductBuilder aProductAltProcessor() {
        return Product.builder()
                .id(new ProductId())
                .inStock(true)
                .name(new ProductName("Core 7 ultra"))
                .price(new Money("2000"));
    }

    public static Product.ProductBuilder aProductAltRamMemory() {
        return Product.builder()
                .id(new ProductId())
                .inStock(true)
                .name(new ProductName("RAM 8GB"))
                .price(new Money("200"));
    }
}
