package com.github.mattthey.mapper;

import com.github.mattthey.api.dto.AuthorDto;
import com.github.mattthey.storage.entity.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toDto(AuthorEntity authorEntity);

    AuthorEntity toEntity(AuthorDto authorDto);
}
