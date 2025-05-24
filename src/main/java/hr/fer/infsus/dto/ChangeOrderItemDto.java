package hr.fer.infsus.dto;

public record ChangeOrderItemDto(
        Long orderId,
        Long productId,
        Long userId,
        int quantity
) {
}
