package com.github.mattthey.service.impl;

import com.github.mattthey.api.dto.AuthorDto;
import com.github.mattthey.api.dto.BookDto;
import com.github.mattthey.mapper.BookMapper;
import com.github.mattthey.service.AuthorService;
import com.github.mattthey.service.BookService;
import com.github.mattthey.storage.entity.BookEntity;
import com.github.mattthey.storage.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorService = authorService;
    }

    @Override
    public List<BookDto> getAllBooks() {
        final var bookDtos = new LinkedList<BookDto>();
        bookRepository.findAll().forEach(book -> bookDtos.add(bookMapper.toDto(book)));
        return Collections.unmodifiableList(bookDtos);
    }

    @Override
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        final AuthorDto author = Objects.requireNonNull(bookDto.author(), "Author is required");
        if (author.id() == null) {
            final AuthorDto createdAuthor = authorService.createAuthor(author);
            bookDto = new BookDto(null, bookDto.title(), createdAuthor, bookDto.category());
        } else {
            final AuthorDto existingAuthor = authorService.getAuthorById(author.id()).orElseThrow();
            bookDto = new BookDto(null, bookDto.title(), existingAuthor, bookDto.category());
        }

        final BookEntity entity = bookMapper.toEntity(bookDto);
        entity.setId(null);
        return bookMapper.toDto(bookRepository.save(entity));
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow();
    }

    @Override
    @Transactional
    public void updateBook(Long id, BookDto bookDto) {
        final AuthorDto author = Objects.requireNonNull(bookDto.author(), "Author is required");
        if (author.id() == null) {
            final AuthorDto createdAuthor = authorService.createAuthor(author);
            bookDto = new BookDto(id, bookDto.title(), createdAuthor, bookDto.category());
        } else {
            final AuthorDto existingAuthor = authorService.getAuthorById(author.id()).orElseThrow();
            bookDto = new BookDto(id, bookDto.title(), existingAuthor, bookDto.category());
        }

        final BookEntity entity = bookMapper.toEntity(bookDto);
        bookRepository.save(entity);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> getAuthorsBooks(Long id) {
        return bookRepository.findBookEntitiesByAuthorId(id).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> getBooksWithSpecifiedCategory(Long id) {
        return bookRepository.findByCategoryIdWithSubcategories(id).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
