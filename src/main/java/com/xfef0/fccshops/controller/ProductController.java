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
@RequestMapping("${api.prefix}/product")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> allProducts = productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Success", allProducts));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product found", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategory(categoryName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found of category: " + categoryName, null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found of brand: " + brandName, null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByName(productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found with name: " + productName, null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found for brand: " + brandName +
                                " and name: " + productName, null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/by-category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String categoryName, @RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(categoryName, brandName);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Products not found for category: " + categoryName +
                                " and brand: " + brandName, null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", products));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/count/by-brand-and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            Long count = productService.countProductsByBrandAndName(brandName, productName);
            return ResponseEntity.ok(new ApiResponse("Products = ", count));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto product) {
        try {
            Product addedProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Product added", addedProduct));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDto product, @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProduct(product, productId);
            return ResponseEntity.ok(new ApiResponse("Product updated", updatedProduct));
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
