package com.xfef0.fccshops.controller;

import com.xfef0.fccshops.dto.ProductDto;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.response.ApiResponse;
import com.xfef0.fccshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDTO(product);
            return ResponseEntity.ok(new ApiResponse("Product found", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> allProducts = productService.getAllProducts();
            List<ProductDto> productDtos = productService.getProductDTOsFromProducts(allProducts);
            return ResponseEntity.ok(new ApiResponse("Success", productDtos));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found of category: " + category, null));
            }
            List<ProductDto> productDtos = productService.getProductDTOsFromProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found", productDtos));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found of brand: " + brand, null));
            }
            List<ProductDto> productDtos = productService.getProductDTOsFromProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found", productDtos));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found with name: " + name, null));
            }
            List<ProductDto> productDtos = productService.getProductDTOsFromProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found", productDtos));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found for brand: " + brand +
                                " and name: " + name, null));
            }
            List<ProductDto> productDtos = productService.getProductDTOsFromProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found", productDtos));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found for category: " + category +
                                " and brand: " + brand, null));
            }
            List<ProductDto> productDtos = productService.getProductDTOsFromProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found", productDtos));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/count/by-brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Products = ", count));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto product) {
        try {
            Product addedProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertToDTO(addedProduct);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Product added", productDto));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDto product, @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProduct(product, productId);
            ProductDto productDto = productService.convertToDTO(updatedProduct);
            return ResponseEntity.ok(new ApiResponse("Product updated", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    private static ResponseEntity<ApiResponse> getInternalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error:", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
