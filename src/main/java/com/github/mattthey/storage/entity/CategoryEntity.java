package com.github.mattthey.storage.entity;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")

public class CategoryEntity {
    private Long id;

    private String title;

    private Long parentCategoryId;
}
