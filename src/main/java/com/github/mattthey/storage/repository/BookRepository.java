package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Iterable<BookEntity> findAll();

    BookEntity save(BookEntity bookEntity);

    Optional<BookEntity> findById(Long id);

    void deleteById(Long id);

    List<BookEntity> findBookEntitiesByAuthorId(Long id);

    List<BookEntity> findByCategoryIdWithSubcategories(Long id);
}
