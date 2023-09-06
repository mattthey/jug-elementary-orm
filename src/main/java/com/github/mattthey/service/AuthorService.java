package com.github.mattthey.service;

import com.github.mattthey.api.dto.AuthorDto;
import com.github.mattthey.api.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    /**
     * Создать автора
     *
     * @param authorDto автор, которого надо создать
     * @return созданный автор
     */
    AuthorDto createAuthor(AuthorDto authorDto);

    /**
     * Изменить сущщность автора
     *
     * @param id идентификатор автора
     * @param authorDto изменяемый автор
     */
    void updateAuthor(Long id, AuthorDto authorDto);

    /**
     * Удалить автора со всеми его книгами
     *
     * @param id идентификатор удаляемого автора
     */
    void deleteAuthor(Long id);

    /**
     * Получить автора по идентификатору
     *
     * @param id идентификатор автора
     * @return автор
     */
    Optional<AuthorDto> getAuthorById(Long id);

    /**
     * Получить всех авторов
     * @return список авторов
     */
    List<AuthorDto> getAllAuthors();
}
