package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.BookEntity;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.github.mattthey.storage.repository.QueriesProvider.*;
import static com.github.mattthey.storage.repository.RepositoryMapper.convertBookEntity;
import static com.github.mattthey.storage.repository.RepositoryMapper.convertCollectionBookEntity;

@Repository
public class BookRepository {

    private final DataSource dataSource;

    public BookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<BookEntity> findAll() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_ALL_BOOKS_WITH_AUTHOR_AND_CATEGORY)) {
            final var resultSet = statement.executeQuery();
            return convertCollectionBookEntity(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BookEntity create(BookEntity bookEntity) {
        final var createdBook = new BookEntity(null, bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getCategory());
        final var author = bookEntity.getAuthor();
        final var authorId = author != null ? author.getId() : null;
        final var category = bookEntity.getCategory();
        final var categoryId = category != null ? category.getId() : null;
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(INSERT_BOOK)) {
            statement.setString(1, bookEntity.getTitle());
            statement.setObject(2, authorId);
            statement.setObject(3, categoryId);
            final var resultSet = statement.executeQuery();
            resultSet.next();
            createdBook.setId(resultSet.getLong(BOOK_ID));
            return createdBook;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<BookEntity> findById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(FIND_BOOK_BY_ID)) {
            statement.setLong(1, id);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(convertBookEntity(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(BookEntity bookEntity) {
        final var author = bookEntity.getAuthor();
        final var authorId = author != null ? author.getId() : null;
        final var category = bookEntity.getCategory();
        final var categoryId = category != null ? category.getId() : null;
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(UPDATE_BOOK)) {
            statement.setString(1, bookEntity.getTitle());
            statement.setObject(2, authorId);
            statement.setObject(3, categoryId);
            statement.setLong(4, bookEntity.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(DELETE_BOOK)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить список книг от автора
     *
     * @param id идентификтор книги
     * @return список книг с указанным автором
     */
    public List<BookEntity> findBookEntitiesByAuthorId(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(FIND_BOOKS_BY_AUTHOR_ID)) {
            statement.setLong(1, id);
            final var resultSet = statement.executeQuery();
            return convertCollectionBookEntity(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookEntity> findByCategoryIdWithSubcategories(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_BOOKS_FROM_SUBCATEGORIES)) {
            statement.setLong(1, id);
            final var resultSet = statement.executeQuery();
            return convertCollectionBookEntity(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
