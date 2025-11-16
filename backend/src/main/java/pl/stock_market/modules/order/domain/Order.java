package pl.stock_market.modules.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal price;
    @Embedded
    Portfolio portfolio;
    @Enumerated(EnumType.STRING)
    OrderType type;
    @Embedded
    OrderAssets assets;
    LocalDateTime created = LocalDateTime.now();

    public Order(BigDecimal price, Portfolio portfolio, OrderType type, OrderAssets assets) {
        this.price = price;
        this.portfolio = portfolio;
        this.type = type;
        this.assets = assets;
    }

    public Order() {}

    public void addQuantity(BigDecimal quantityToAdd) {
        assets.addQuantity(quantityToAdd);
    }

    public BigDecimal getQuantity() {
        return assets.getQuantity();
    }

    public boolean isActivated() {
        return assets.isActivated();
    }

    public enum OrderType {
        BUY,
        SELL
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", portfolio=" + portfolio +
                ", type=" + type +
                ", assets=" + assets +
                '}';
    }

}
