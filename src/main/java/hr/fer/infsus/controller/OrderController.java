package hr.fer.infsus.controller;

import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public List<OrderDto> getOrdersByUserId(@PathVariable final Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderByOrderId(@PathVariable final Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping()
    public OrderDto createOrder(@RequestBody final OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @PutMapping("/{orderId}")
    public OrderDto updateOrder(@PathVariable final Long orderId, @RequestBody final OrderDto orderDto) {
        return orderService.updateOrder(orderId, orderDto);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable final Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
