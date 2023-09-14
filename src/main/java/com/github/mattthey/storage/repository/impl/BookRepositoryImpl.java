package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.BookEntity;
import com.github.mattthey.storage.repository.BookRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final DataSource dataSource;

    public BookRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<BookEntity> findAll() {
        return null;
    }

    @Override
    public Optional<BookEntity> findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
    }

    /**
     * Получить список книг от автора
     *
     * @param id идентификтор книги
     * @return список книг с указанным автором
     */
    @Override
    public List<BookEntity> findBookEntitiesByAuthorId(Long id) {
        return null;
    }

    @Override
    public List<BookEntity> findByCategoryIdWithSubcategories(Long id) {
        return null;
    }

    @Override
    public BookEntity save(BookEntity bookEntity) {
        return null;
    }

    private BookEntity create(BookEntity bookEntity) {
        return null;
    }

    private void update(BookEntity bookEntity) {
    }
}
