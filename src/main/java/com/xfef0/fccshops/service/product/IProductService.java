package com.xfef0.fccshops.service.product;

import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    ProductDTO addProduct(ProductDTO request);
    ProductDTO getProductById(Long id);
    void deleteProductById(Long id);
    ProductDTO updateProduct(ProductDTO request, Long productId);
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(String category);
    List<ProductDTO> getProductsByBrand(String brand);
    List<ProductDTO> getProductsByName(String name);
    List<ProductDTO> getProductsByCategoryAndBrand(String category, String brand);
    List<ProductDTO> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);
}
