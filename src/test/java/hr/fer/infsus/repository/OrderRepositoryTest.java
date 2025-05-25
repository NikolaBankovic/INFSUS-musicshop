package hr.fer.infsus.repository;

import hr.fer.infsus.model.Order;
import hr.fer.infsus.model.User;
import hr.fer.infsus.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private User buildAndSaveUser(String username) {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(username + "@mail.com");
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    private Order buildOrder(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setDeliveryAddress("Some address");
        order.setCreditCardNumber("1234-5678-9876-5432");
        return order;
    }

    @Test
    @DisplayName("save should persist an Order")
    void save_shouldPersistOrder() {
        User user = buildAndSaveUser("user1");
        Order order = buildOrder(user);

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isNotNull();
        Optional<Order> found = orderRepository.findById(savedOrder.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getDeliveryAddress()).isEqualTo("Some address");
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("findAllByUserId should return orders for a given user")
    void findAllByUserId_shouldReturnOrders() {
        User user = buildAndSaveUser("user2");
        Order order = buildOrder(user);
        orderRepository.save(order);

        List<Order> found = orderRepository.findAllByUserId(user.getId());
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("findAllByUserId should return empty list for user without orders")
    void findAllByUserId_shouldReturnEmptyList() {
        User user = buildAndSaveUser("user3");

        List<Order> found = orderRepository.findAllByUserId(user.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("delete should remove an Order by id")
    void delete_shouldRemoveOrderById() {
        User user = buildAndSaveUser("user4");
        Order order = buildOrder(user);
        Order savedOrder = orderRepository.save(order);

        Long orderId = savedOrder.getId();
        assertThat(orderRepository.findById(orderId)).isPresent();

        orderRepository.deleteById(orderId);
        assertThat(orderRepository.findById(orderId)).isNotPresent();
    }
}
