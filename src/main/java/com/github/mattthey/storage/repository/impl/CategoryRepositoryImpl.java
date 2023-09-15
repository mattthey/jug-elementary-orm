package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.CategoryEntity;
import com.github.mattthey.storage.repository.CategoryRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.mattthey.gen.public_.Tables.CATEGORY;
import static com.github.mattthey.storage.repository.impl.QueriesProvider.ID;
import static com.github.mattthey.storage.repository.impl.QueriesProvider.getCategoryHierarchy;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.select;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final DSLContext context;

    @Autowired
    public CategoryRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    public List<CategoryEntity> findAll() {
        return context.selectFrom(CATEGORY)
                .fetch()
                .stream()
                .map(RepositoryMapper::toEntity)
                .toList();
    }

    public Optional<CategoryEntity> findById(Long id) {
        return context.selectFrom(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .fetchOptional()
                .map(RepositoryMapper::toEntity);
    }

    public List<CategoryEntity> findAllSubcategories(Long id) {
        final var idParam = param(ID, id);
        final var cte = getCategoryHierarchy(idParam);
        return context.withRecursive(cte)
                .selectFrom(cte)
                .where(idParam.ne(cte.field(ID, Long.class)))
                .fetch()
                .stream()
                .map(record -> new CategoryEntity(record.get(CATEGORY.ID), record.get(CATEGORY.TITLE), record.get(CATEGORY.PARENT_CATEGORY_ID)))
                .toList();
    }

    public void deleteCategoryWithSubcategories(Long id) {
        final var idParam = param(ID, id);
        final var cte = getCategoryHierarchy(idParam);
        context.withRecursive(cte)
                .delete(CATEGORY)
                .where(CATEGORY.ID.in(
                        select(cte.field(ID, Long.class)).from(cte)
                )).execute();
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
        final var createdCategory = context.insertInto(CATEGORY)
                .set(RepositoryMapper.toRecord(categoryEntity))
                .returning()
                .fetchOne();
        return RepositoryMapper.toEntity(Objects.requireNonNull(createdCategory));
    }

    private void update(CategoryEntity categoryEntity) {
        context.update(CATEGORY)
                .set(RepositoryMapper.toRecord(categoryEntity))
                .where(CATEGORY.ID.eq(categoryEntity.getId()))
                .execute();
    }
}
