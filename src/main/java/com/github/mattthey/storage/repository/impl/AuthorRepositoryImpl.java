package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.AuthorEntity;
import com.github.mattthey.storage.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.github.mattthey.storage.repository.impl.QueriesProvider.*;
import static com.github.mattthey.storage.repository.impl.RepositoryMapper.convertAuthor;
import static com.github.mattthey.storage.repository.impl.RepositoryMapper.convertCollectionAuthors;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final DataSource dataSource;

    @Autowired
    public AuthorRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorEntity> findAll() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_ALL_AUTHOR_ENTITY)) {
            final var resultSet = statement.executeQuery();
            return convertCollectionAuthors(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorEntity> findById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(GET_AUTHOR_BY_ID)) {
            statement.setLong(1, id);
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(convertAuthor(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        if (authorEntity.getId() == null) {
            return create(authorEntity);
        } else {
            return update(authorEntity);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(DELETE_AUTHOR)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthorEntity update(AuthorEntity authorEntity) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(UPDATE_AUTHOR)) {
            statement.setString(1, authorEntity.getName());
            statement.setLong(2, authorEntity.getId());
            final var resultSet = statement.executeQuery();
            resultSet.next();
            return convertAuthor(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthorEntity create(AuthorEntity authorEntity) {
        final var createdAuthor = new AuthorEntity(null, authorEntity.getName());
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(INSERT_AUTHOR)) {
            statement.setString(1, authorEntity.getName());
            final var resultSet = statement.executeQuery();
            resultSet.next();
            createdAuthor.setId(resultSet.getLong(AUTHOR_ID));
            return createdAuthor;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
