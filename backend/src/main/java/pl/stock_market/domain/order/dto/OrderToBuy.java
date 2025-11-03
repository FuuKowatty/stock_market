package pl.stock_market.domain.order.dto;

import pl.stock_market.infrastructure.application.dto.OrderDto;

import java.math.BigDecimal;

public record OrderToBuy(OrderDto order, BigDecimal neededQuantity) {
}
