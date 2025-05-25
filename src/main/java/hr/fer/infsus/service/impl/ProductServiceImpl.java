package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.ProductDto;
import hr.fer.infsus.dto.filter.ProductFilter;
import hr.fer.infsus.model.Category;
import hr.fer.infsus.model.Product;
import hr.fer.infsus.repository.CategoryRepository;
import hr.fer.infsus.repository.ProductRepository;
import hr.fer.infsus.repository.specification.ProductSpecification;
import hr.fer.infsus.service.ProductService;
import hr.fer.infsus.util.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductDto getProductById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with ID(%d) not found!", id)));

        return productMapper.productToProductDto(product);
    }

    public List<ProductDto> getAllProducts(final ProductFilter filter) {
        final ProductSpecification<Product> specification = new ProductSpecification<>(filter);
        final List<Product> products = productRepository.findAll(specification);
        return productMapper.productsToProductDtos(products);
    }

    public ProductDto createProduct(final ProductDto productDto) {
        final Product product = productMapper.productDtoToProduct(productDto);
        return productMapper.productToProductDto(productRepository.save(product));
    }

    public ProductDto updateProduct(final Long id, final ProductDto productDto) {
        final Product product = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Product with ID(%d) not found!", id)));

        final Category category = categoryRepository.findById(productDto.category().id()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category with ID(%d) not found!", productDto.category().id())));

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setImage(productDto.image());
        product.setTimesVisited(productDto.timesVisited());
        product.setCategory(category);

        return productMapper.productToProductDto(productRepository.save(product));
    }

    public void deleteProduct(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with ID(%d) not found!", id)));
        productRepository.delete(product);
    }

    public void incrementTimesVisitedForProduct(final Long id) {
        productRepository.incrementTimesVisited(id);
    }
}
