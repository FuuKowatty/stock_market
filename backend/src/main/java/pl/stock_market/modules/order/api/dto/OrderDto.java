package pl.stock_market.modules.order.api.dto;

import lombok.Getter;
import pl.stock_market.modules.order.domain.Order;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;

@Getter
public class OrderDto {
    Long id;
    BigDecimal quantity;
    BigDecimal price;
    Portfolio portfolio;
    Order.OrderType type;

    public OrderDto(BigDecimal quantity, BigDecimal price, Portfolio portfolio, Order.OrderType type) {
        this.quantity = quantity;
        this.price = price;
        this.portfolio = portfolio;
        this.type = type;
    }

    public OrderDto(Long id, BigDecimal quantity, BigDecimal price, Portfolio portfolio, Order.OrderType type) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.portfolio = portfolio;
        this.type = type;
    }
}
