package hr.fer.infsus.util.mapper;


import hr.fer.infsus.dto.CategoryDto;
import hr.fer.infsus.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category CategoryDtoToCategory(CategoryDto categoryDto);
    CategoryDto CategoryToCategoryDto(Category category);
    List<Category> CategoryDtosToCategories(List<CategoryDto> categoryDtos);
    List<CategoryDto> CategoriesToCategoryDtos(List<Category> category);
}
