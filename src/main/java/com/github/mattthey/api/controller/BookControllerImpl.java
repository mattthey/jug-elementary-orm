package com.github.mattthey.api.controller;

import com.github.mattthey.api.BookController;
import com.github.mattthey.api.dto.BookDto;
import com.github.mattthey.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookControllerImpl implements BookController {
    private final BookService bookService;

    @Autowired
    public BookControllerImpl(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookService.getBookById(id);
    }

    @Override
    public void updateBook(Long id, BookDto bookDto) {
        bookService.updateBook(id, bookDto);
    }

    @Override
    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }

    @Override
    public List<BookDto> getBooksWithSpecifiedCategory(Long id) {
        return bookService.getBooksWithSpecifiedCategory(id);
    }

    @Override
    public List<BookDto> getBooksFromAuthor(Long id) {
        return bookService.getAuthorsBooks(id);
    }
}
