package hr.fer.infsus.controller;

import hr.fer.infsus.dto.CategoryDto;
import hr.fer.infsus.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable final Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody final CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PutMapping("/{categoryId}")
    public CategoryDto updateCategory(@PathVariable final Long categoryId, @RequestBody final CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryId, categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable final Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
