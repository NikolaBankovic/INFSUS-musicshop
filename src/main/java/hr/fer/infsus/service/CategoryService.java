package hr.fer.infsus.service;

import hr.fer.infsus.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(final Long id);
    CategoryDto createCategory(final CategoryDto categoryDto);
    CategoryDto updateCategory(final Long id, final CategoryDto categoryDto);
    void deleteCategory(final Long id);
}
