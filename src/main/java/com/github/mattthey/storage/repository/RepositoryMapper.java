package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.AuthorEntity;
import com.github.mattthey.storage.entity.BookEntity;
import com.github.mattthey.storage.entity.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.github.mattthey.storage.repository.QueriesProvider.*;

public class RepositoryMapper {
    private RepositoryMapper()
    {
    }


    static List<AuthorEntity> convertCollectionAuthors(ResultSet resultSet) throws SQLException {
        final var result = new LinkedList<AuthorEntity>();
        while (resultSet.next()) {
            result.add(convertAuthor(resultSet));
        }
        return result;
    }

    static AuthorEntity convertAuthor(ResultSet resultSet) throws SQLException {
        return new AuthorEntity(resultSet.getLong(AUTHOR_ID), resultSet.getString(AUTHOR_NAME));
    }

    static List<CategoryEntity> convertCollectionCategory(ResultSet resultSet) throws SQLException {
        final var result = new LinkedList<CategoryEntity>();
        while (resultSet.next()) {
            result.add(convertCategory(resultSet));
        }
        return result;
    }

    static CategoryEntity convertCategory(ResultSet resultSet) throws SQLException {
        final var categoryId = resultSet.getObject(CATEGORY_ID);
        if (categoryId == null) {
            return null;
        }
        return new CategoryEntity(
                (Long) categoryId,
                resultSet.getString(CATEGORY_TITLE),
                (Long) resultSet.getObject(CATEGORY_PARENT_CATEGORY_ID));
    }

    static List<BookEntity> convertCollectionBookEntity(ResultSet resultSet) throws SQLException {
        final var result = new LinkedList<BookEntity>();
        while (resultSet.next()) {
            result.add(convertBookEntity(resultSet));
        }
        return result;
    }

    static BookEntity convertBookEntity(ResultSet resultSet) throws SQLException {
        return new BookEntity(
                resultSet.getLong(BOOK_ID),
                resultSet.getString(BOOK_TITLE),
                convertAuthor(resultSet),
                convertCategory(resultSet)
        );
    }
}
