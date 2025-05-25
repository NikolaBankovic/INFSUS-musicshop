package hr.fer.infsus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItemsList=new ArrayList<>();

    private double totalPrice;

    private String creditCardNumber;

    private Date orderDate;

    private String deliveryAddress;

    public void setTotalAmount() {
        if (orderItemsList != null) {
            this.totalPrice = orderItemsList.stream()
                    .mapToDouble(orderItem -> orderItem.getPrice()* orderItem.getQuantity())
                    .sum();
        } else {
            this.totalPrice =  0;
        }
    }
}
