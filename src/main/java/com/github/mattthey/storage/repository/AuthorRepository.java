package com.github.mattthey.storage.repository;

import com.github.mattthey.storage.entity.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {
    Iterable<AuthorEntity> findAll();

    Optional<AuthorEntity> findById(Long id);

    AuthorEntity save(AuthorEntity authorEntity);

    void deleteById(Long id);
}
