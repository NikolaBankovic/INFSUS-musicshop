package hr.fer.infsus.dto;

public record OrderItemDto(
        Long id,
        ProductDto product,
        double price,
        int quantity

) {
}