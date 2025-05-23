package hr.fer.infsus.dto;

public record ProductDto(
        Long id,
        String name,
        double price,
        String description,
        String image,
        Long timesVisited,
        CategoryDto category
) {
}