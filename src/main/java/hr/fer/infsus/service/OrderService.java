package hr.fer.infsus.service;

import hr.fer.infsus.dto.ChangeOrderItemDto;
import hr.fer.infsus.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAllOrders();
    List<OrderDto> getOrdersByUserId(Long userId);
    OrderDto getOrderById(Long orderId);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    OrderDto addUpdateOrderItem(final ChangeOrderItemDto changeOrderItemDto);
    OrderDto removeOrderItem(final ChangeOrderItemDto changeOrderItemDto);
    void deleteOrder(Long orderId);
}