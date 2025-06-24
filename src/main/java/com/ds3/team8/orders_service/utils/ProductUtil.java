package com.ds3.team8.orders_service.utils;

import com.ds3.team8.orders_service.client.ProductClient;
import com.ds3.team8.orders_service.client.dtos.ProductResponse;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProductUtil.class);

    private ProductUtil() {
        
    }

    public static ProductResponse validateProduct(ProductClient productClient, Long productId) {
        try {
            ProductResponse product = productClient.getProductById(productId);
            logger.info("Producto con ID {} validado correctamente", productId);
            return product;
        } catch (FeignException e) {
            logger.error("Error al validar el producto: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo validar el producto", e);
        }
    }
}
