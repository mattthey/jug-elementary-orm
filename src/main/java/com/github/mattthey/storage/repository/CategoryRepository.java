package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    CategoryEntity save(CategoryEntity categoryEntity);

    Iterable<CategoryEntity> findAll();

    Optional<CategoryEntity> findById(Long id);

    List<CategoryEntity> findAllSubcategories(Long id);

    void deleteCategoryWithSubcategories(Long id);
}
