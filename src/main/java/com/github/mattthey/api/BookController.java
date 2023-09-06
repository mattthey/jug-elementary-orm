package com.github.mattthey.api;

import com.github.mattthey.api.dto.BookDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public interface BookController {
    List<BookDto> getAllBooks(Integer offset, Integer limit);

    BookDto getBookById(int id);

    BookDto createBook(BookDto bookDto);

    BookDto updateBook(long id, BookDto bookDto);

    BookDto deleteBook(Long id);
}
