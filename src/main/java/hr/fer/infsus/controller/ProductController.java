package hr.fer.infsus.controller;

import hr.fer.infsus.dto.ProductDto;
import hr.fer.infsus.dto.filter.ProductFilter;
import hr.fer.infsus.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable final Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(final ProductFilter filter) {
        return productService.getAllProducts(filter);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody final ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable final Long productId, @RequestBody final ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable final Long id) {
        productService.deleteProduct(id);
    }

    @PatchMapping("/{id}")
    public void incrementTimesVisitedForProduct(@PathVariable final Long id) {
        productService.incrementTimesVisitedForProduct(id);
    }
}