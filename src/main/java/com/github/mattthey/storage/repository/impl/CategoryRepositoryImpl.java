package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.CategoryEntity;
import com.github.mattthey.storage.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final DataSource dataSource;

    @Autowired
    public CategoryRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CategoryEntity> findAll() {
        return null;
    }

    public Optional<CategoryEntity> findById(Long id) {
        return null;
    }

    public List<CategoryEntity> findAllSubcategories(Long id) {
        return null;
    }

    public void deleteCategoryWithSubcategories(Long id) {

    }

    public CategoryEntity save(CategoryEntity categoryEntity) {
        return null;
    }

    private CategoryEntity create(CategoryEntity categoryEntity) {
        return null;
    }

    private void update(CategoryEntity categoryEntity) {
    }
}
