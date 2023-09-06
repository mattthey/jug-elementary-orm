package com.github.mattthey.api.controller;

import com.github.mattthey.api.AuthorController;
import com.github.mattthey.api.dto.AuthorDto;
import com.github.mattthey.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorControllerImpl implements AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorControllerImpl(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public List<AuthorDto> getAllAuthors(Integer offset, Integer limit) {
        return authorService.getAllAuthors();
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        return authorService.getAuthorById(id).orElseThrow();
    }

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        return authorService.createAuthor(authorDto);
    }

    @Override
    public void updateAuthor(Long id, AuthorDto authorDto) {
        authorService.updateAuthor(id, authorDto);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorService.deleteAuthor(id);
    }
}
