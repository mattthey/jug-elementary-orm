package com.github.mattthey.api.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record BookDto(
        Long id,
        String title,
        List<AuthorDto> authors,
        OffsetDateTime publicationDate,
        String description
)
{

}
