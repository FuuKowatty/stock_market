package pl.stock_market.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "orders")
class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal price;
    BigDecimal value;
    @Embedded
    Portfolio portfolio;
    @Embedded
    OrderAssets assets;

    public Order(Long id, BigDecimal price, BigDecimal assets) {
        this.id = id;
        this.price = price;
        this.assets = new OrderAssets();
        this.value = price.multiply(assets);
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

}
