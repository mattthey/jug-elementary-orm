package com.github.mattthey.api;

import com.github.mattthey.api.dto.BookDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/books")
public interface BookController {

    @GetMapping("/")
    List<BookDto> getAllBooks();

    @PostMapping("/")
    BookDto createBook(@RequestBody BookDto bookDto);

    @GetMapping("/{id}")
    BookDto getBookById(@PathVariable Long id);

    @PutMapping("/{id}")
    void updateBook(@PathVariable Long id, @RequestBody BookDto bookDto);

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable Long id);

    @GetMapping("/category/{id}")
    List<BookDto> getBooksWithSpecifiedCategory(@PathVariable Long id);

    @GetMapping("/authors_books/{id}")
    List<BookDto> getBooksFromAuthor(@PathVariable Long id);
}
