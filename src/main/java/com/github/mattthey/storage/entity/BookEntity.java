package com.github.mattthey.storage.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Data

public class BookEntity {
    private Long id;
    private String title;
    private AuthorEntity author;
    private CategoryEntity category;
}
