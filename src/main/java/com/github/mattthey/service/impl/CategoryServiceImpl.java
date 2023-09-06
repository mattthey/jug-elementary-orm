package com.github.mattthey.service.impl;

import com.github.mattthey.api.dto.CategoryDto;
import com.github.mattthey.mapper.CategoryMapper;
import com.github.mattthey.service.CategoryService;
import com.github.mattthey.storage.entity.CategoryEntity;
import com.github.mattthey.storage.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с категориями
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        final CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);
        categoryEntity.setId(null);
        return categoryMapper.toDto(categoryRepository.create(categoryEntity));
    }

    @Override
    public void updateCategory(Long id, CategoryDto categoryDto) {
        final CategoryEntity storedCategory = categoryRepository.findById(id).orElseThrow();
        if (categoryDto.parentCategoryId() != null && !Objects.equals(storedCategory.getParentCategoryId(), categoryDto.parentCategoryId())) {
            final Set<Long> subcategoriesIds = getSubcategories(id).stream()
                    .map(CategoryDto::id)
                    .collect(Collectors.toSet());
            if (subcategoriesIds.contains(categoryDto.parentCategoryId())) {
                throw new RuntimeException("Recursive dependencies forbidden");
            }
        }
        final CategoryEntity categoryEntity = categoryMapper.toEntity(categoryDto);
        categoryEntity.setId(id);
        categoryRepository.update(categoryEntity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteCategoryWithSubcategories(id);
    }

    @Override
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    @Override
    public List<CategoryDto> getSubcategories(Long id) {
        return categoryRepository.findAllSubcategories(id).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        final List<CategoryDto> result = new LinkedList<>();
        categoryRepository.findAll().forEach(entity -> result.add(categoryMapper.toDto(entity)));
        return Collections.unmodifiableList(result);
    }
}
