package com.github.mattthey.api.controller;

import com.github.mattthey.api.BookController;
import com.github.mattthey.api.dto.BookDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
public class BookControllerImpl implements BookController {

    private final List<BookDto> books = new ArrayList<>(List.of(
            new BookDto(1L, "Book 1", List.of(), null, "desc1"),
            new BookDto(2L, "Book 2", List.of(), null, "desc2"),
            new BookDto(3L, "Book 3", List.of(), null, "desc3")
    ));

    @Override
    @GetMapping("/")
    public List<BookDto> getAllBooks(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit) {
        return books.subList(offset, Math.min(offset + limit, books.size()));
    }

    @Override
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable int id) {
        return books.get(id + 1);
    }

    @Override
    @PostMapping("/")
    public BookDto createBook(@RequestBody BookDto bookDto) {
        books.add(bookDto);
        return bookDto;
    }

    @Override
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable long id, @RequestBody BookDto bookDto) {
        books.removeIf(b -> b.id() == bookDto.id());
        books.add(bookDto);
        return bookDto;
    }

    @Override
    @DeleteMapping("/{id}")
    public BookDto deleteBook(@PathVariable Long id) {
        BookDto deleted = null;
        for (BookDto book : books) {
            if (Objects.equals(book.id(), id)) {
                books.remove(book);
                deleted = book;
            }
        }
        return deleted;
    }
}
