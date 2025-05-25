package hr.fer.infsus.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.infsus.dto.ChangeOrderItemDto;
import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.model.*;
import hr.fer.infsus.repository.CategoryRepository;
import hr.fer.infsus.repository.OrderRepository;
import hr.fer.infsus.repository.ProductRepository;
import hr.fer.infsus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_USER);
        userRepository.save(testUser);

        testCategory = new Category();
        testCategory.setName("Test");
        categoryRepository.saveAndFlush(testCategory);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setDescription("Opis");
        testProduct.setImage("slika.jpg");
        testProduct.setCategory(testCategory);
        productRepository.save(testProduct);
    }

    @Test
    void createOrder_and_getOrderById() throws Exception {
        OrderDto orderDto = new OrderDto(
                null,
                new UserDto(testUser.getId(), testUser.getFirstName(), testUser.getLastName(), testUser.getUsername(), testUser.getEmail(), testUser.getPassword(), testUser.getRole()),
                List.of(),
                0.0,
                "****3456",
                new Date(),
                "Test Address"
        );
        String orderJson = objectMapper.writeValueAsString(orderDto);

        String responseJson = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryAddress").value("Test Address"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDto savedOrder = objectMapper.readValue(responseJson, OrderDto.class);

        assertThat(savedOrder.id()).isNotNull();
        Order orderFromDb = orderRepository.findById(savedOrder.id()).orElseThrow();
        assertThat(orderFromDb.getDeliveryAddress()).isEqualTo("Test Address");

        mockMvc.perform(get("/api/order/" + savedOrder.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryAddress").value("Test Address"))
                .andExpect(jsonPath("$.user.id").value(testUser.getId()));
    }

    @Test
    void getAllOrders_and_getOrdersByUserId() throws Exception {
        Order order1 = new Order();
        order1.setUser(testUser);
        order1.setCreditCardNumber("****1111");
        order1.setOrderDate(new Date());
        order1.setDeliveryAddress("Adresa 1");
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setUser(testUser);
        order2.setCreditCardNumber("****2222");
        order2.setOrderDate(new Date());
        order2.setDeliveryAddress("Adresa 2");
        orderRepository.save(order2);

        mockMvc.perform(get("/api/order/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        mockMvc.perform(get("/api/order/user/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void addOrderItem_shouldAddItemToOrder() throws Exception {
        OrderDto orderDto = new OrderDto(
                null,
                new UserDto(testUser.getId(), testUser.getFirstName(), testUser.getLastName(),
                        testUser.getUsername(), testUser.getEmail(), testUser.getPassword(), testUser.getRole()),
                List.of(),
                0.0,
                "****3456",
                new Date(),
                "Order Address"
        );
        String orderJson = objectMapper.writeValueAsString(orderDto);

        String savedOrderJson = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        OrderDto savedOrder = objectMapper.readValue(savedOrderJson, OrderDto.class);

        var changeOrderItemDto = new ChangeOrderItemDto(
                savedOrder.id(),
                testProduct.getId(),
                testUser.getId(),
                2
        );
        String changeOrderItemJson = objectMapper.writeValueAsString(changeOrderItemDto);

        String responseJson = mockMvc.perform(post("/api/order/orderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeOrderItemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderItemsList.length()").value(1))
                .andExpect(jsonPath("$.orderItemsList[0].product.id").value(testProduct.getId()))
                .andExpect(jsonPath("$.orderItemsList[0].quantity").value(2))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDto returnedOrderDto = objectMapper.readValue(responseJson, OrderDto.class);
        Order orderFromDb = orderRepository.findById(savedOrder.id()).orElseThrow();
        assertThat(orderFromDb.getOrderItemsList()).hasSize(1);
        assertThat(orderFromDb.getOrderItemsList().get(0).getProduct().getId()).isEqualTo(testProduct.getId());
        assertThat(orderFromDb.getOrderItemsList().get(0).getQuantity()).isEqualTo(2);
    }


    @Test
    void removeOrderItem_shouldRemoveItemFromOrder() throws Exception {
        OrderDto orderDto = new OrderDto(
                null,
                new UserDto(testUser.getId(), testUser.getFirstName(), testUser.getLastName(), testUser.getUsername(), testUser.getEmail(), testUser.getPassword(), testUser.getRole()),
                List.of(),
                0.0,
                "****5555",
                new Date(),
                "Delete Item Address"
        );
        String orderJson = objectMapper.writeValueAsString(orderDto);
        String savedOrderJson = mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        OrderDto savedOrder = objectMapper.readValue(savedOrderJson, OrderDto.class);

        var changeOrderItemDto = new ChangeOrderItemDto(
                savedOrder.id(),
                testProduct.getId(),
                testUser.getId(),
                1
        );
        String changeOrderItemJson = objectMapper.writeValueAsString(changeOrderItemDto);

        mockMvc.perform(post("/api/order/orderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeOrderItemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderItemsList.length()").value(1));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/order/orderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(changeOrderItemJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderItemsList.length()").value(0));
    }


    @Test
    void updateOrder_shouldUpdateAddressAndCard() throws Exception {
        Order order = new Order();
        order.setUser(testUser);
        order.setCreditCardNumber("****1111");
        order.setOrderDate(new Date());
        order.setDeliveryAddress("Old address");
        orderRepository.save(order);

        OrderDto updateDto = new OrderDto(
                order.getId(),
                new UserDto(testUser.getId(), testUser.getFirstName(), testUser.getLastName(), testUser.getUsername(), testUser.getEmail(), testUser.getPassword(), testUser.getRole()),
                List.of(),
                0.0,
                "****2222",
                new Date(),
                "New Address"
        );
        String updateJson = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/api/order/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryAddress").value("New Address"))
                .andExpect(jsonPath("$.creditCardNumber").value("****2222"));
    }

}
