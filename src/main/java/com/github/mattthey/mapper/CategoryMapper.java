package com.github.mattthey.mapper;

import com.github.mattthey.api.dto.CategoryDto;
import com.github.mattthey.storage.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
//    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDto toDto(CategoryEntity categoryEntity);

//    @Mapping(target = "parentCategory.id", source = "parentCategoryId")
    CategoryEntity toEntity(CategoryDto categoryDto);
}
