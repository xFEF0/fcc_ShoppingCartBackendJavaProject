package com.xfef0.fccshops.service.product;

import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Category;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.repository.ProductRepository;
import com.xfef0.fccshops.dto.ProductDto;
import com.xfef0.fccshops.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    public static final String PRODUCT_NOT_FOUND = "Product not found!";
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Override
    public Product addProduct(ProductDto request) {
        Category requestCategory = request.getCategory();
        Objects.requireNonNull(requestCategory);
        Category category = Optional.ofNullable(categoryService.getCategoryByName(requestCategory.getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(requestCategory.getName());
                    return categoryService.addCategory(newCategory);
                });

        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(ProductDto request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public void deleteProductById(Long id) {
        Optional.ofNullable(getProductById(id))
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);});
    }

    @Override
    public Product updateProduct(ProductDto request, Long productId) {
        return Optional.ofNullable(getProductById(productId))
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    private Product updateExistingProduct(Product existingProduct, ProductDto request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        Category requestCategory = request.getCategory();
        Objects.requireNonNull(requestCategory);
        Category category = Optional.ofNullable(categoryService.getCategoryByName(requestCategory.getName()))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
