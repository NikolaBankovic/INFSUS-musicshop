package hr.fer.infsus.dto;

import java.util.Date;
import java.util.List;

public record OrderDto(
        Long id,
        UserDto user,
        List<OrderItemDto> orderItemsList,
        double totalPrice,
        String creditCardNumber,
        Date orderDate,
        String deliveryAddress
) {
}