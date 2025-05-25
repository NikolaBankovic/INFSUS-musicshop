package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.ChangeOrderItemDto;
import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.model.Order;
import hr.fer.infsus.model.OrderItem;
import hr.fer.infsus.model.Product;
import hr.fer.infsus.model.User;
import hr.fer.infsus.repository.OrderItemRepository;
import hr.fer.infsus.repository.OrderRepository;
import hr.fer.infsus.repository.ProductRepository;
import hr.fer.infsus.repository.UserRepository;
import hr.fer.infsus.util.mapper.OrderMapper;
import hr.fer.infsus.util.validator.OrderValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private UserRepository userRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders_shouldReturnDtos() {
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderDto> dtos = List.of(mock(OrderDto.class), mock(OrderDto.class));

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.ordersToOrderDtos(orders)).thenReturn(dtos);

        List<OrderDto> result = orderService.getAllOrders();

        assertThat(result).isEqualTo(dtos);
        verify(orderRepository).findAll();
        verify(orderMapper).ordersToOrderDtos(orders);
    }

    @Test
    void getAllOrders_shouldReturnEmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        when(orderMapper.ordersToOrderDtos(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<OrderDto> result = orderService.getAllOrders();
        assertThat(result).isEmpty();
    }

    @Test
    void getOrderById_shouldReturnDto() {
        Order order = new Order();
        OrderDto dto = mock(OrderDto.class);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.getOrderById(1L);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getOrderById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.getOrderById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void createOrder_shouldWork() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Date now = new Date();
        String card = "1234123412341234";
        String address = "Somewhere";

        UserDto userDto = new UserDto(userId, "", "", "", "", "", null);
        OrderDto inputDto = new OrderDto(null,userDto,List.of(),0.0,card,now,address);
        Order outputOrder = new Order();
        outputOrder.setId(100L);

        OrderDto outputDto = mock(OrderDto.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(outputOrder);
        when(orderMapper.orderToOrderDto(outputOrder)).thenReturn(outputDto);

        OrderDto result = orderService.createOrder(inputDto);

        assertThat(result).isEqualTo(outputDto);
        verify(userRepository).findById(userId);
        verify(orderValidator).creditCardNumber(card);
        verify(orderValidator).notFutureDate(any(Date.class));
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).orderToOrderDto(outputOrder);
    }


    @Test
    void createOrder_shouldThrowIfUserNotFound() {
        UserDto userDto = new UserDto(2L, "", "", "", "", "", null);
        OrderDto inputDto = mock(OrderDto.class);
        when(inputDto.user()).thenReturn(userDto);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.createOrder(inputDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id 2 not found");
    }

    @Test
    void updateOrder_shouldWork() {
        Long orderId = 1L;
        Long userId = 2L;
        Date now = new Date();
        User user = new User();
        user.setId(userId);
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        UserDto userDto = new UserDto(userId, "ime", "prezime", "email", "username", "pass", null);
        OrderDto inputDto = new OrderDto(orderId,userDto,List.of(),150.0,"****1234",now,"test-address");
        OrderDto outputDto = mock(OrderDto.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(orderMapper.orderToOrderDto(existingOrder)).thenReturn(outputDto);

        OrderDto result = orderService.updateOrder(orderId, inputDto);

        assertThat(result).isEqualTo(outputDto);
        assertThat(existingOrder.getUser()).isEqualTo(user);
        assertThat(existingOrder.getDeliveryAddress()).isEqualTo("test-address");
        assertThat(existingOrder.getCreditCardNumber()).isEqualTo("****1234");
        assertThat(existingOrder.getOrderDate()).isEqualTo(now);

        verify(orderValidator).creditCardNumber("****1234");
        verify(orderValidator).notFutureDate(now);
        verify(orderRepository).save(existingOrder);
        verify(orderMapper).orderToOrderDto(existingOrder);
    }

    @Test
    void updateOrder_shouldThrowIfOrderNotFound() {
        OrderDto inputDto = mock(OrderDto.class);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.updateOrder(1L, inputDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void addUpdateOrderItem_shouldAddNewItem() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 2L, 3L, 2);
        Product product = new Product(); product.setId(2L); product.setPrice(99.9);
        Order order = new Order(); order.setId(1L);
        OrderDto mappedOrderDto = mock(OrderDto.class);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderIdAndProductId(1L, 2L)).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderDto(order)).thenReturn(mappedOrderDto);

        OrderDto result = orderService.addUpdateOrderItem(dto);

        assertThat(result).isEqualTo(mappedOrderDto);
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(orderMapper).orderToOrderDto(order);
    }



    @Test
    void addUpdateOrderItem_shouldUpdateExistingItem() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 2L, 3L, 5);
        Product product = new Product(); product.setId(2L); product.setPrice(11.1);
        Order order = new Order(); order.setId(1L);
        OrderItem orderItem = new OrderItem(); orderItem.setQuantity(1);
        OrderDto mappedOrderDto = mock(OrderDto.class);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderIdAndProductId(1L, 2L)).thenReturn(Optional.of(orderItem));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderDto(order)).thenReturn(mappedOrderDto);

        OrderDto result = orderService.addUpdateOrderItem(dto);

        assertThat(orderItem.getQuantity()).isEqualTo(5);
        assertThat(result).isEqualTo(mappedOrderDto);
        verify(orderItemRepository).save(orderItem);
        verify(orderMapper).orderToOrderDto(order);
    }

    @Test
    void addUpdateOrderItem_shouldThrowIfProductNotFound() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 99L, 3L, 2);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.addUpdateOrderItem(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product with id 99 not found");
    }

    @Test
    void addUpdateOrderItem_shouldThrowIfOrderNotFound() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(88L, 2L, 3L, 2);
        Product product = new Product(); product.setId(2L);
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(88L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.addUpdateOrderItem(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 88 not found");
    }

    @Test
    void removeOrderItem_shouldRemoveItem() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 2L, 5L, 2);

        Product product = new Product(); product.setId(2L);
        Order order = new Order(); order.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        List<OrderItem> orderItemsList = new ArrayList<>();
        orderItemsList.add(orderItem);
        order.setOrderItemsList(orderItemsList);

        Order savedOrder = new Order();
        OrderDto mappedDto = mock(OrderDto.class);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderIdAndProductId(1L, 2L)).thenReturn(Optional.of(orderItem));
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.orderToOrderDto(savedOrder)).thenReturn(mappedDto);

        OrderDto result = orderService.removeOrderItem(dto);

        assertThat(order.getOrderItemsList()).doesNotContain(orderItem);
        assertThat(result).isEqualTo(mappedDto);
        verify(orderItemRepository).delete(orderItem);
        verify(orderRepository, times(2)).save(order);
        verify(orderMapper).orderToOrderDto(savedOrder);
    }

    @Test
    void removeOrderItem_shouldThrowIfProductNotFound() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 2L, 5L, 2);
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.removeOrderItem(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product with id 2 not found");
    }

    @Test
    void removeOrderItem_shouldThrowIfOrderNotFound() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 2L, 5L, 2);

        Product product = new Product(); product.setId(2L);
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.removeOrderItem(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 1 not found");
    }

    @Test
    void removeOrderItem_shouldDoNothingIfOrderItemNotFound() {
        ChangeOrderItemDto dto = new ChangeOrderItemDto(1L, 2L, 5L, 2);
        Product product = new Product(); product.setId(2L);
        Order order = new Order(); order.setId(1L);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderIdAndProductId(1L, 2L)).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);
        OrderDto mappedDto = mock(OrderDto.class);
        when(orderMapper.orderToOrderDto(order)).thenReturn(mappedDto);

        OrderDto result = orderService.removeOrderItem(dto);

        assertThat(result).isEqualTo(mappedDto);
        verify(orderItemRepository, never()).delete(any());
    }

    @Test
    void deleteOrder_shouldCallRepository() {
        orderService.deleteOrder(1L);
        verify(orderRepository).deleteById(1L);
    }
}
