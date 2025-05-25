package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.CategoryDto;
import hr.fer.infsus.model.Category;
import hr.fer.infsus.repository.CategoryRepository;
import hr.fer.infsus.service.CategoryService;
import hr.fer.infsus.util.mapper.CategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();

        return categoryMapper.CategoriesToCategoryDtos(categories);
    }

    public CategoryDto getCategoryById(final Long id){
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category with id %s not found", id)));

        return categoryMapper.CategoryToCategoryDto(category);
    }

    public CategoryDto createCategory(final CategoryDto categoryDto){
        final Category category = categoryMapper.CategoryDtoToCategory(categoryDto);

        return categoryMapper.CategoryToCategoryDto(categoryRepository.save(category));
    }

    public CategoryDto updateCategory(final Long id, final CategoryDto categoryDto){
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category with id %s not found", id)));

        category.setName(categoryDto.name());

        return categoryMapper.CategoryToCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(final Long id){
        categoryRepository.deleteById(id);
    }
}
