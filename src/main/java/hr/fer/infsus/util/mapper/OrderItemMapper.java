package hr.fer.infsus.util.mapper;

import hr.fer.infsus.dto.OrderItemDto;
import hr.fer.infsus.model.CartItem;
import hr.fer.infsus.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {
    OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto);
    OrderItemDto orderItemToOrderItemDto(OrderItem orderItem);
    List<OrderItem> orderItemDtosToOrderItems(List<OrderItemDto> orderItemDtos);
    List<OrderItemDto> orderItemsToOrderItemDtos(List<OrderItem> orderItemsList);

    @Mapping(target = "id", ignore = true)
    OrderItem cartItemToOrderItem(CartItem cartItem);
    List<OrderItem> cartItemsToOrderItems(List<CartItem> cartItems);
}
