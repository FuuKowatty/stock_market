package pl.stock_market.modules.order.api.dto;

import java.math.BigDecimal;

public record OrderToBuy(OrderDto order, BigDecimal neededQuantity) {
}
