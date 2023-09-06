package com.github.mattthey.mapper;

import com.github.mattthey.api.dto.BookDto;
import com.github.mattthey.storage.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AuthorMapper.class, CategoryMapper.class })
public interface BookMapper {
    BookDto toDto(BookEntity book);

    BookEntity toEntity(BookDto bookDto);
}
