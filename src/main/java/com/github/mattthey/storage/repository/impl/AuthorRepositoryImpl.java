package com.github.mattthey.storage.repository.impl;

import com.github.mattthey.storage.entity.AuthorEntity;
import com.github.mattthey.storage.repository.AuthorRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.mattthey.gen.public_.Tables.AUTHOR;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final DSLContext context;

    @Autowired
    public AuthorRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public List<AuthorEntity> findAll() {
        return context.selectFrom(AUTHOR)
                .fetch()
                .stream()
                .map(RepositoryMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<AuthorEntity> findById(Long id) {
        return context.selectFrom(AUTHOR)
                .where(AUTHOR.ID.eq(id))
                .fetchOptional()
                .map(RepositoryMapper::toEntity);
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        if (authorEntity.getId() == null) {
            return create(authorEntity);
        } else {
            update(authorEntity);
            return new AuthorEntity(authorEntity.getId(), authorEntity.getName());
        }
    }

    @Override
    public void deleteById(Long id) {
        context.delete(AUTHOR).where(AUTHOR.ID.eq(id)).execute();
    }

    private void update(AuthorEntity authorEntity) {
        context.update(AUTHOR)
                .set(RepositoryMapper.toRecord(authorEntity))
                .where(AUTHOR.ID.eq(authorEntity.getId()))
                .execute();
    }

    private AuthorEntity create(AuthorEntity authorEntity) {
        final var authorRecord = context.insertInto(AUTHOR)
                .set(RepositoryMapper.toRecord(authorEntity))
                .returning()
                .fetchOne();
        return RepositoryMapper.toEntity(Objects.requireNonNull(authorRecord));
    }
}
