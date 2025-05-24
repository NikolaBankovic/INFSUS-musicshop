package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.model.Order;
import hr.fer.infsus.model.OrderItem;
import hr.fer.infsus.model.User;
import hr.fer.infsus.repository.OrderItemRepository;
import hr.fer.infsus.repository.OrderRepository;
import hr.fer.infsus.repository.UserRepository;
import hr.fer.infsus.service.OrderService;
import hr.fer.infsus.util.mapper.OrderItemMapper;
import hr.fer.infsus.util.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public List<OrderDto> getAllOrders() {
        final List<Order> orders = orderRepository.findAll();
        return orderMapper.ordersToOrderDtos(orders);
    }

    public List<OrderDto> getOrdersByUserId(Long userId) {
        final List<Order> orders = orderRepository.findAllByUserId(userId);
        return orderMapper.ordersToOrderDtos(orders);
    }

    public OrderDto getOrderById(Long orderId) {
        final Order order = orderRepository.findById(orderId).orElseThrow(()->
                new EntityNotFoundException(String.format("Order with id %s not found", orderId)));

        return orderMapper.orderToOrderDto(order);
    }

    public OrderDto createOrder(OrderDto orderDto) {
        final Order order = new Order();
        final List<OrderItem> orderItems = orderItemMapper.orderItemDtosToOrderItems(orderDto.orderItemsList());
        orderItems.forEach(orderItem -> {
            orderItem.setOrder(order);
            orderItem.setPrice(orderItem.getProduct().getPrice());
        });

        final User user = userRepository.findById(orderDto.user().id()).orElseThrow(()->
                new EntityNotFoundException(String.format("User with id %s not found", orderDto.user().id())));

        order.setUser(user);
        order.setOrderItemsList(orderItems);
        order.setTotalAmount();
        order.setCreditCardNumber(String.format("****%s", orderDto.creditCardNumber()));
        order.setOrderDate(new Date());
        order.setDeliveryAddress(orderDto.deliveryAddress());

        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    public OrderDto updateOrder(final Long id, final OrderDto orderDto) {
        final Order order = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Order with ID(%d) doesn't exist.", id)));

        final User user = userRepository.findById(orderDto.user().id()).orElseThrow(()->
                new EntityNotFoundException(String.format("User with id %s not found", orderDto.user().id())));

        List<OrderItem> existingOrderItems = orderItemRepository.findByOrderId(order.getId());
        Map<Long, OrderItem> existingItemsByProductId = existingOrderItems.stream()
                .collect(Collectors.toMap(
                        oi -> oi.getProduct().getId(),
                        oi -> oi
                ));

        List<OrderItem> newOrderItems = orderItemMapper.orderItemDtosToOrderItems(orderDto.orderItemsList());

        for (OrderItem newOrderItem : newOrderItems) {
            Long productId = newOrderItem.getProduct().getId();
            if (existingItemsByProductId.containsKey(productId)) {
                int existingQuantity = existingItemsByProductId.get(productId).getQuantity();
                newOrderItem.setQuantity(newOrderItem.getQuantity() + existingQuantity);
            }
            newOrderItem.setOrder(order);
        }

        order.setUser(user);
        order.setOrderItemsList(newOrderItems);
        order.setOrderDate(orderDto.orderDate());
        order.setCreditCardNumber(orderDto.creditCardNumber());
        order.setTotalAmount();
        order.setDeliveryAddress(orderDto.deliveryAddress());

        return orderMapper.orderToOrderDto(orderRepository.save(order));
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

}