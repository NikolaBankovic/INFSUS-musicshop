package hr.fer.infsus.util.mapper;

import hr.fer.infsus.dto.ProductDto;
import hr.fer.infsus.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

   ProductDto productToProductDto(Product product);
   Product productDtoToProduct(ProductDto productDto);
   List<ProductDto> productsToProductDtos(List<Product> products);
}
