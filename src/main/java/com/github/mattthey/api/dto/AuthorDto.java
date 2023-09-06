package com.github.mattthey.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record AuthorDto(
        Long id,
        String name,
        OffsetDateTime dateOfBirth,
        OffsetDateTime dateOfDeath
) {
}
