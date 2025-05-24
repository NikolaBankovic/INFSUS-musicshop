package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.model.Order;
import hr.fer.infsus.model.OrderItem;
import hr.fer.infsus.model.Product;
import hr.fer.infsus.model.User;
import hr.fer.infsus.repository.OrderItemRepository;
import hr.fer.infsus.repository.OrderRepository;
import hr.fer.infsus.repository.ProductRepository;
import hr.fer.infsus.repository.UserRepository;
import hr.fer.infsus.service.OrderService;
import hr.fer.infsus.util.mapper.OrderItemMapper;
import hr.fer.infsus.util.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

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

    @Override
    public OrderDto updateOrder(final Long id, final OrderDto orderDto) {
        final Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));

        final User user = userRepository.findById(orderDto.user().id())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + orderDto.user().id() + " not found"));
        existingOrder.setUser(user);

        existingOrder.setDeliveryAddress(orderDto.deliveryAddress());

        existingOrder.setCreditCardNumber(String.format("%s", orderDto.creditCardNumber()));

        existingOrder.getOrderItemsList().clear();


        List<OrderItem> updatedItems = orderItemMapper.orderItemDtosToOrderItems(orderDto.orderItemsList());
        updatedItems.forEach(item -> {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product with id " + item.getProduct().getId() + " not found"));
            item.setProduct(product);

            item.setOrder(existingOrder);
            item.setPrice(product.getPrice());
        });

        existingOrder.getOrderItemsList().addAll(updatedItems);

        existingOrder.setTotalAmount();
        existingOrder.setOrderDate(new Date());

        return orderMapper.orderToOrderDto(orderRepository.save(existingOrder));
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

}