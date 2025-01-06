package com.xfef0.fccshops.service.product;

import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.dto.ProductDto;

import java.util.List;

public interface IProductService {
    Product addProduct(ProductDto request);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductDto request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategoryName(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryNameAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}
