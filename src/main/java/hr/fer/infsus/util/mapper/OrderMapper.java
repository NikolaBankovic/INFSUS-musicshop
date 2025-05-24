package hr.fer.infsus.util.mapper;

import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, UserMapper.class})
public interface OrderMapper {
    List<Order> orderDtosToOrders(List<OrderDto> orderDtos);
    List<OrderDto> ordersToOrderDtos(List<Order> orders);
    Order orderDtoToOrder(OrderDto orderDto);
    OrderDto orderToOrderDto(Order order);
}
