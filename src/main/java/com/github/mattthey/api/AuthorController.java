package com.github.mattthey.api;

import com.github.mattthey.api.dto.AuthorDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/authors")
public interface AuthorController {

    @GetMapping("/")
    List<AuthorDto> getAllAuthors();

    @GetMapping("/{id}")
    AuthorDto getAuthorById(@PathVariable Long id);

    @PostMapping("/")
    AuthorDto createAuthor(@RequestBody AuthorDto authorDto);

    @PutMapping("/{id}")
    void updateAuthor(@PathVariable Long id, @RequestBody AuthorDto authorDto);

    @DeleteMapping("/{id}")
    void deleteAuthor(@PathVariable Long id);
}
