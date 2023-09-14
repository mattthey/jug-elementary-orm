package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.CategoryEntity;
import com.github.mattthey.storage.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.github.mattthey.storage.repository.impl.QueriesProvider.*;
import static com.github.mattthey.storage.repository.impl.RepositoryMapper.convertCategory;
import static com.github.mattthey.storage.repository.impl.RepositoryMapper.convertCollectionCategory;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final DataSource dataSource;

    @Autowired
    public CategoryRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<CategoryEntity> findAll() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_ALL_CATEGORY)) {
            final var resultSet = statement.executeQuery();
            return convertCollectionCategory(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CategoryEntity> findById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_CATEGORY_BY_ID)) {
            statement.setLong(1, id);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(convertCategory(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CategoryEntity> findAllSubcategories(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_ALL_SUBCATEGORIES)) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            final var resultSet = statement.executeQuery();
            return convertCollectionCategory(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCategoryWithSubcategories(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(DELETE_CATEGORY_WITH_SUBCATEGORIES)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryEntity save(CategoryEntity categoryEntity) {
        if (categoryEntity.getId() == null) {
            return create(categoryEntity);
        } else {
            update(categoryEntity);
            return new CategoryEntity(categoryEntity.getId(), categoryEntity.getTitle(), categoryEntity.getParentCategoryId());
        }
    }

    private CategoryEntity create(CategoryEntity categoryEntity) {
        final var createdCategoryEntity = new CategoryEntity(null, categoryEntity.getTitle(), categoryEntity.getParentCategoryId());
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(INSERT_CATEGORY)) {
            statement.setString(1, categoryEntity.getTitle());
            statement.setObject(2, categoryEntity.getParentCategoryId());
            final var resultSet = statement.executeQuery();
            resultSet.next();
            createdCategoryEntity.setId(resultSet.getLong(CATEGORY_ID));
            return createdCategoryEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(CategoryEntity categoryEntity) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(UPDATE_CATEGORY)) {
            statement.setLong(1, categoryEntity.getParentCategoryId());
            statement.setString(2, categoryEntity.getTitle());
            statement.setLong(3, categoryEntity.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
