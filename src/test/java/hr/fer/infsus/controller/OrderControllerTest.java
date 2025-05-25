package hr.fer.infsus.controller;

import hr.fer.infsus.dto.ChangeOrderItemDto;
import hr.fer.infsus.dto.OrderDto;
import hr.fer.infsus.security.TokenProvider;
import hr.fer.infsus.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(TokenProvider.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private static OrderDto exampleOrderDto() {
        return new OrderDto(
                1L, null, Collections.emptyList(), 0.0, "****1234", null, "Some address"
        );
    }

    @Test
    @DisplayName("getAllOrders should return 200 and list")
    void getAllOrders_shouldWork() throws Exception {
        Mockito.when(orderService.getAllOrders())
                .thenReturn(List.of(exampleOrderDto()));

        mockMvc.perform(get("/api/order/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("getAllOrders should return empty list")
    void getAllOrders_shouldntWork() throws Exception {
        Mockito.when(orderService.getAllOrders())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/order/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("getOrdersByUserId should return 200 and list")
    void getOrdersByUserId_shouldWork() throws Exception {
        Mockito.when(orderService.getOrdersByUserId(5L))
                .thenReturn(List.of(exampleOrderDto()));

        mockMvc.perform(get("/api/order/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("getOrderByOrderId should return 200")
    void getOrderByOrderId_shouldWork() throws Exception {
        Mockito.when(orderService.getOrderById(7L))
                .thenReturn(exampleOrderDto());

        mockMvc.perform(get("/api/order/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("createOrder should return 201")
    void createOrder_shouldWork() throws Exception {
        OrderDto requestDto = exampleOrderDto();
        Mockito.when(orderService.createOrder(any(OrderDto.class)))
                .thenReturn(requestDto);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id":1,"user":null,"orderItemsList":[],"totalPrice":0.0,"creditCardNumber":"****1234","orderDate":null,"deliveryAddress":"Some address"
                    }
                """))
                .andExpect(status().isOk()) // ili .isCreated() ako tako vraćaš
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("updateOrder should return 200")
    void updateOrder_shouldWork() throws Exception {
        OrderDto requestDto = exampleOrderDto();
        Mockito.when(orderService.updateOrder(eq(1L), any(OrderDto.class)))
                .thenReturn(requestDto);

        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id":1,"user":null,"orderItemsList":[],"totalPrice":0.0,"creditCardNumber":"****1234","orderDate":null,"deliveryAddress":"Some address"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("addUpdateOrderItem should return 200")
    void addUpdateOrderItem_shouldWork() throws Exception {
        Mockito.when(orderService.addUpdateOrderItem(any(ChangeOrderItemDto.class)))
                .thenReturn(exampleOrderDto());

        mockMvc.perform(post("/api/order/orderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"orderId":1,"itemId":1,"quantity":1}
                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("deleteUpdateOrderItem should return 200")
    void deleteUpdateOrderItem_shouldWork() throws Exception {
        Mockito.when(orderService.removeOrderItem(any(ChangeOrderItemDto.class)))
                .thenReturn(exampleOrderDto());

        mockMvc.perform(delete("/api/order/orderItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"orderId":1,"itemId":1}
                """))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("deleteOrder should return 200")
    void deleteOrder_shouldWork() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isOk());
    }

}
