package com.xfef0.fccshops.service.product;

import com.xfef0.fccshops.dto.ImageDTO;
import com.xfef0.fccshops.exception.AlreadyExistsException;
import com.xfef0.fccshops.exception.MissingValueException;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Category;
import com.xfef0.fccshops.model.Image;
import com.xfef0.fccshops.model.Product;
import com.xfef0.fccshops.repository.ProductRepository;
import com.xfef0.fccshops.dto.ProductDTO;
import com.xfef0.fccshops.service.category.ICategoryService;
import com.xfef0.fccshops.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private static final String PRODUCT_NOT_FOUND = "Product not found!";
    private static final String CATEGORY_NOT_FOUND = "Category not found!";
    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final IImageService imageService;
    private final ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO request) {
        try {
            Category requestCategory = Objects.requireNonNull(request.getCategory(), "Category");
            String requestName = Objects.requireNonNull(request.getName(), "Name");
            String requestBrand = Objects.requireNonNull(request.getBrand(), "Brand");
            List<ProductDTO> productsByBrandAndName = getProductsByBrandAndName(requestBrand, requestName);
            if (!productsByBrandAndName.isEmpty()) {
                throw new AlreadyExistsException("Product with name = " + requestName +
                        " and brand = " + requestBrand + " already exists!" +
                        "\nTry updating it");
            }
            Category category = Optional.ofNullable(categoryService.getCategoryByName(requestCategory.getName()))
                    .orElseGet(() -> {
                        Category newCategory = new Category(requestCategory.getName());
                        return categoryService.addCategory(newCategory);
                    });
            request.setCategory(category);
            Product product = createProduct(request, category);
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        } catch (NullPointerException e) {
            throw new MissingValueException(e.getMessage() + "  is required.");
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public ProductDTO getProductDTOById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Transactional
    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ProductDTO updateProduct(ProductDTO request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByName(String name) {
        return productRepository.findByName(name).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ProductDTO> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    private Product createProduct(ProductDTO request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getPrice(),
                request.getInventory(),
                category
        );
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDto = modelMapper.map(product, ProductDTO.class);
        List<Image> images = imageService.getImageByProductId(product.getId());
        List<ImageDTO> imageDTOs = images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .toList();
        productDto.setImages(imageDTOs);
        return productDto;
    }

    private Product updateExistingProduct(Product existingProduct, ProductDTO request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        Category requestCategory = request.getCategory();
        Objects.requireNonNull(requestCategory);
        Category category = Optional.ofNullable(categoryService.getCategoryByName(requestCategory.getName()))
                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        existingProduct.setCategory(category);
        return existingProduct;
    }
}
