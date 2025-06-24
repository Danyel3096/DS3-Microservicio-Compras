package com.ds3.team8.orders_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ds3.team8.orders_service.client.dtos.ProductResponse;
import com.ds3.team8.orders_service.client.dtos.StockRequest;
import com.ds3.team8.orders_service.config.FeignClientInterceptor;

@FeignClient(name = "products-service", configuration = FeignClientInterceptor.class)
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);

     @PostMapping("/api/v1/products/validate-stock")
    Boolean validateStock(@RequestBody List<StockRequest> requests);
}
