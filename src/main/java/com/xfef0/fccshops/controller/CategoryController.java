package com.xfef0.fccshops.controller;

import com.xfef0.fccshops.exception.AlreadyExistsException;
import com.xfef0.fccshops.exception.ResourceNotFoundException;
import com.xfef0.fccshops.model.Category;
import com.xfef0.fccshops.response.ApiResponse;
import com.xfef0.fccshops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Categories found", categories));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Success", newCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Category found", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName) {
        try {
            Category category = categoryService.getCategoryByName(categoryName);
            return ResponseEntity.ok(new ApiResponse("Category found", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok(new ApiResponse("Category deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return getInternalServerErrorResponse();
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Category updated", updatedCategory));
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
