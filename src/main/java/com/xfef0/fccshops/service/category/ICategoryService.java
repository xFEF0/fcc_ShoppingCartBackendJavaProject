package com.xfef0.fccshops.service.category;

import com.xfef0.fccshops.model.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(Category category);
    Category updateCategory(Category category, Long categoryId);
    void deleteCategory(Long categoryId);
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
}
