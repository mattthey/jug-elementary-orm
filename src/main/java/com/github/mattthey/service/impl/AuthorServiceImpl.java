package com.github.mattthey.service.impl;

import com.github.mattthey.api.dto.AuthorDto;
import com.github.mattthey.mapper.AuthorMapper;
import com.github.mattthey.service.AuthorService;
import com.github.mattthey.storage.entity.AuthorEntity;
import com.github.mattthey.storage.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public Optional<AuthorDto> getAuthorById(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        final AuthorEntity entity = authorMapper.toEntity(authorDto);
        entity.setId(null);
        return authorMapper.toDto(authorRepository.create(entity));
    }

    @Override
    public void updateAuthor(Long id, AuthorDto authorDto) {
        final Optional<AuthorEntity> authorOpt = authorRepository.findById(id);
        if (authorOpt.isEmpty()) {
            throw new RuntimeException("Author with id %d not found".formatted(id));
        }
        final AuthorEntity authorEntity = authorOpt.get();
        authorEntity.setName(authorDto.name());
        authorRepository.update(authorEntity);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
