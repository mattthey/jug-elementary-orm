package com.github.mattthey.service;

import com.github.mattthey.api.dto.BookDto;

import java.util.List;

public interface BookService {

    /**
     * Получить список книг
     *
     * @return список книг
     */
    List<BookDto> getAllBooks();

    /**
     * Создать книгу
     * @param bookDto книга для создания
     * @return созданная книга
     */
    BookDto createBook(BookDto bookDto);

    /**
     * Получить книгу по идентификатору
     *
     * @param id идентификатор книги
     * @return книга
     */
    BookDto getBookById(Long id);

    /**
     * Изменить книгу
     *
     * @param id идентификатор книги
     * @param bookDto книга
     */
    void updateBook(Long id, BookDto bookDto);

    /**
     * Удалить книгу
     *
     * @param id идентификатор книги
     */
    void deleteBook(Long id);

    /**
     * Получение списка книг автора
     *
     * @param id идентификатор автора
     * @return список книг, написанных автором
     */
    List<BookDto> getAuthorsBooks(Long id);

    /**
     * Получить список книг с указанной категорией и её подкатегориями
     *
     * @param id идентификатор категории
     * @return список книг с указанной категорией и подкатегориями
     */
    List<BookDto> getBooksWithSpecifiedCategory(Long id);
}
