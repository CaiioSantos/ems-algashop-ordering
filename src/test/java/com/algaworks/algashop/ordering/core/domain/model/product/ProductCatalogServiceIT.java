package com.algaworks.algashop.ordering.core.domain.model.product;

import com.algaworks.algashop.ordering.core.domain.model.commons.Money;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.web.product.client.http.ProductCatalogAPIClient;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.web.product.client.http.ProductResponse;
import com.algaworks.algashop.ordering.utils.TestContainerPostgresSqlConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestContainerPostgresSqlConfig.class)
class ProductCatalogServiceIT {

    @Autowired
    private ProductCatalogService productCatalogService;

    @MockitoBean
    private ProductCatalogAPIClient productCatalogAPIClient;

    @Test
    public void concurrencyLimitTest() throws InterruptedException {
        ProductId productId = new ProductId(UUID.randomUUID());
        // Mock the API client to return the product
        Mockito.when(productCatalogAPIClient.getById(productId.value())).thenReturn(null);

        try(ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            executorService.submit(() -> productCatalogService.ofId(productId));
            executorService.submit(() -> productCatalogService.ofId(productId));
            executorService.submit(() -> productCatalogService.ofId(productId));
            executorService.submit(() -> productCatalogService.ofId(productId));
            executorService.submit(() -> productCatalogService.ofId(productId));
            executorService.submit(() -> productCatalogService.ofId(productId));
            executorService.awaitTermination(30, TimeUnit.SECONDS);
            executorService.shutdown();
            }

    }
}