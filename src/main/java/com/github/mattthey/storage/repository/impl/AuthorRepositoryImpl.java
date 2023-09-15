package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.AuthorEntity;
import com.github.mattthey.storage.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final DataSource dataSource;

    @Autowired
    public AuthorRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorEntity> findAll() {
        return null;
    }

    @Override
    public Optional<AuthorEntity> findById(Long id) {
        return null;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
    }

    private void update(AuthorEntity authorEntity) {
    }

    private AuthorEntity create(AuthorEntity authorEntity) {
        return null;
    }
}
