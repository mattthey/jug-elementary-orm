package com.github.mattthey.api;

import com.github.mattthey.api.dto.CategoryDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/category")
public interface CategoryController {

    @PostMapping("/")
    CategoryDto createCategory(@RequestBody CategoryDto categoryDto);

    @PutMapping("/{id}")
    void updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto);

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable Long id);

    @GetMapping("/{id}")
    CategoryDto getCategoryById(@PathVariable Long id);

    @GetMapping("/")
    List<CategoryDto> getAllCategories();

    @GetMapping("/subcategories/{id}")
    List<CategoryDto> getSubcategories(@PathVariable Long id);
}
