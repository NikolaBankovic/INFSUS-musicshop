package hr.fer.infsus.dto;

public record OrderItemDto(
        Long id,
        OrderDto order,
        ProductDto product,
        double price,
        int quantity

) {
}