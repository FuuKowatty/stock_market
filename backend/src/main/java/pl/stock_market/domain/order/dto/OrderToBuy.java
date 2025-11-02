package pl.stock_market.domain.order.dto;

import lombok.Builder;
import pl.stock_market.infrastructure.application.dto.OrderDto;

import java.math.BigDecimal;

@Builder
public record OrderToBuy(OrderDto order, BigDecimal neededQuantity) {
}
