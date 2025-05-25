package hr.fer.infsus.service;

import hr.fer.infsus.dto.ProductDto;
import hr.fer.infsus.dto.filter.ProductFilter;

import java.util.List;

public interface ProductService {

    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts(ProductFilter filter);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    void incrementTimesVisitedForProduct(Long id);
}
