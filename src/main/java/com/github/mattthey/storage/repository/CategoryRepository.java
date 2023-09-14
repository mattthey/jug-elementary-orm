package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.CategoryEntity;
import com.github.mattthey.storage.repository.impl.QueriesProvider;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    CategoryEntity save(CategoryEntity categoryEntity);

    Iterable<CategoryEntity> findAll();

    Optional<CategoryEntity> findById(Long id);

    @Query(nativeQuery = true, value = QueriesProvider.GET_ALL_SUBCATEGORIES)
    List<CategoryEntity> findAllSubcategories(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = QueriesProvider.DELETE_CATEGORY_WITH_SUBCATEGORIES)
    void deleteCategoryWithSubcategories(@Param("id") Long id);
}
