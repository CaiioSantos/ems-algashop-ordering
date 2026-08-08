package com.algaworks.algashop.ordering.infrastructure.config.resilience;

import com.algaworks.algashop.ordering.infrastructure.config.exceptionhandler.BadGatewayException;
import com.algaworks.algashop.ordering.infrastructure.config.exceptionhandler.GatewayTimeoutException;
import org.springframework.cloud.circuitbreaker.retry.FrameworkRetryCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.retry.RetryPolicy;

import java.time.Duration;

@Configuration
public class SpringCircuitBreakerConfig {

    public static final String PRODUCT_CATALOG_CB = "productCatalogCB";
    public static final String SHIPPING_COST_CB = "shippingCostCB";

    @Bean
    public Customizer<FrameworkRetryCircuitBreakerFactory> defaultCustomizer() {
        RetryPolicy retryPolicy = RetryPolicy.builder()
                .maxRetries(3)
                .multiplier(2)
                .delay(Duration.ofSeconds(3))
                .includes(GatewayTimeoutException.class, BadGatewayException.ServerErrorException.class)
                .build();
        return factory -> {

            factory.configure(builder -> builder
                    .retryPolicy(retryPolicy)
                    .openTimeout(Duration.ofSeconds(10))
                    .resetTimeout(Duration.ofSeconds(25))
                    .build(), PRODUCT_CATALOG_CB);

            factory.configure(builder -> builder
                    .retryPolicy(retryPolicy)
                    .openTimeout(Duration.ofSeconds(10))
                    .resetTimeout(Duration.ofSeconds(25))
                    .build(), SHIPPING_COST_CB);
        };
    }
}
