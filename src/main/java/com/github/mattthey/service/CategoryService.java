package com.github.mattthey.service;

import com.github.mattthey.api.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с категориями
 */
public interface CategoryService {
    /**
     * Создать категорию
     *
     * @param categoryDto данные категории
     * @return созданная категория
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * Изменить катогорию. Должна стоять проверка на то, чтобы не было рекурсий
     *
     * @param id идентификатор категории
     * @param categoryDto данные категории
     */
    void updateCategory(Long id, CategoryDto categoryDto);

    /**
     * Удалить категорию и её подкатегории
     * @param id идентификатор категории
     */
    void deleteCategory(Long id);

    /**
     * Получение категории по идентификтаору
     *
     * @param id идентификатор
     * @return категория
     */
    Optional<CategoryDto> getCategoryById(Long id);

    /**
     * Получить подкатегории отеносительно указанной
     *
     * @param id идентификатор категории от которой нужно начать поиски
     * @return подкатегории
     */
    List<CategoryDto> getSubcategories(Long id);

    /**
     * @return список всех категорий
     */
    List<CategoryDto> getAllCategories();
}
