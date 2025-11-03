package pl.stock_market.domain.order;

import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

public class OrderBuilder {
    private BigDecimal price = BigDecimal.valueOf(100);
    private BigDecimal quantity = BigDecimal.ONE;
    private Portfolio portfolio = new Portfolio(1L, 1L, 1L);
    private boolean active = true;

    public static OrderBuilder anOrder() {
        return new OrderBuilder();
    }
    public OrderBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
    public OrderBuilder withPrice(String price) {
        this.price = new BigDecimal(price);
        return this;
    }
    public OrderBuilder withQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }
    public OrderBuilder withPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }
    public OrderBuilder withPortfolio(Long userId, Long walletId, Long stockId) {
        this.portfolio = new Portfolio(userId, walletId, stockId);
        return this;
    }
    public OrderBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }

    Order build() {
        Order order = new Order();
        order.price = this.price;
        order.assets = new OrderAssets(this.quantity, this.active);
        order.portfolio = this.portfolio;
        order.value = this.price.multiply(this.quantity);
        return order;
    }

}
