package com.github.mattthey.storage.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")

public class AuthorEntity {
    private Long id;
    private String name;
}
