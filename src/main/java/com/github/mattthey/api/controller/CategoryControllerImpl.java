package com.github.mattthey.api.controller;

import com.github.mattthey.api.CategoryController;
import com.github.mattthey.api.dto.CategoryDto;
import com.github.mattthey.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryControllerImpl implements CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @Override
    public void updateCategory(Long id, CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryService.deleteCategory(id);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryService.getCategoryById(id).orElseThrow();
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Override
    public List<CategoryDto> getSubcategories(Long id) {
        return categoryService.getSubcategories(id);
    }
}
