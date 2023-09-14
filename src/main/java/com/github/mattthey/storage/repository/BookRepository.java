package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.BookEntity;
import com.github.mattthey.storage.repository.impl.QueriesProvider;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
    Iterable<BookEntity> findAll();

    BookEntity save(BookEntity bookEntity);

    Optional<BookEntity> findById(Long id);

    void deleteById(Long id);

    List<BookEntity> findBookEntitiesByAuthorId(Long id);

    @Query(nativeQuery = true, value = QueriesProvider.GET_BOOKS_FROM_SUBCATEGORIES)
    List<BookEntity> findByCategoryIdWithSubcategories(@Param("id") Long id);
}
