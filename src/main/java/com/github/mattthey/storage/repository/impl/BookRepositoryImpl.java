package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.BookEntity;
import com.github.mattthey.storage.repository.BookRepository;
import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.Record3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.mattthey.gen.public_.Tables.*;
import static com.github.mattthey.storage.repository.impl.QueriesProvider.ID;
import static com.github.mattthey.storage.repository.impl.QueriesProvider.getCategoryHierarchy;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.select;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final DSLContext context;

    @Autowired
    public BookRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public List<BookEntity> findAll() {
        return context.select(BOOK, AUTHOR, CATEGORY)
                .from(BOOK)
                .join(AUTHOR).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .leftJoin(CATEGORY).on(BOOK.CATEGORY_ID.eq(CATEGORY.ID))
                .fetch()
                .stream()
                .map(RepositoryMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return context.select(BOOK, AUTHOR, CATEGORY)
                .from(BOOK)
                .join(AUTHOR).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .leftJoin(CATEGORY).on(BOOK.CATEGORY_ID.eq(CATEGORY.ID))
                .where(BOOK.ID.eq(id))
                .fetchOptional()
                .map(RepositoryMapper::toEntity);
    }

    @Override
    public void deleteById(Long id) {
        context.deleteFrom(BOOK).where(BOOK.ID.eq(id)).execute();
    }

    /**
     * Получить список книг от автора
     *
     * @param id идентификтор книги
     * @return список книг с указанным автором
     */
    @Override
    public List<BookEntity> findBookEntitiesByAuthorId(Long id) {
        return context.select(BOOK, AUTHOR, CATEGORY)
                .from(BOOK)
                .join(AUTHOR).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .leftJoin(CATEGORY).on(BOOK.CATEGORY_ID.eq(CATEGORY.ID))
                .where(AUTHOR.ID.eq(id))
                .fetch()
                .stream()
                .map(RepositoryMapper::toEntity)
                .toList();
    }

    @Override
    public List<BookEntity> findByCategoryIdWithSubcategories(Long id) {
        final Param<Long> idParam = param(ID, id);
        final CommonTableExpression<Record3<Long, String, Long>> cte = getCategoryHierarchy(idParam);
        return context.withRecursive(cte)
                .select(BOOK, AUTHOR, CATEGORY)
                .from(BOOK)
                .join(CATEGORY).on(BOOK.CATEGORY_ID.eq(CATEGORY.ID))
                .join(AUTHOR).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .where(BOOK.CATEGORY_ID.in(
                        select(cte.field(ID, Long.class)).from(cte)
                ))
                .fetch()
                .stream()
                .map(RepositoryMapper::toEntity)
                .toList();
    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        if (bookEntity.getId() == null) {
            return create(bookEntity);
        } else {
            update(bookEntity);
            return new BookEntity(bookEntity.getId(), bookEntity.getTitle(),
                    bookEntity.getAuthor(), bookEntity.getCategory());
        }
    }

    private BookEntity create(BookEntity bookEntity) {
        final var createdBookId = context.insertInto(BOOK)
                .set(RepositoryMapper.toRecord(bookEntity))
                .returning(BOOK.ID)
                .fetchOne();
        return new BookEntity(Objects.requireNonNull(createdBookId).getId(), bookEntity.getTitle(),
                bookEntity.getAuthor(), bookEntity.getCategory());
    }

    private void update(BookEntity bookEntity) {
        context.update(BOOK)
                .set(RepositoryMapper.toRecord(bookEntity))
                .where(BOOK.ID.eq(bookEntity.getId()))
                .execute();
    }
}
