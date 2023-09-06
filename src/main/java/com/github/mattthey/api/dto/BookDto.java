package com.github.mattthey.api.dto;

public record BookDto(Long id, String title, AuthorDto author, CategoryDto category) {}
