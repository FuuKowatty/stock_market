package pl.stock_market.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "orders")
class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal price;
    @Setter
    BigDecimal quantity;
    @Enumerated(EnumType.STRING)
    OrderType type;
    BigDecimal value;
    @Embedded
    Portfolio portfolio;
    @Setter
    boolean active = true;

    public Order(Long id, BigDecimal price, BigDecimal quantity, OrderType type) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.value = price.multiply(quantity);
    }

    public Order() {}

    public enum OrderType {
        BUY,
        SELL
    }
}
