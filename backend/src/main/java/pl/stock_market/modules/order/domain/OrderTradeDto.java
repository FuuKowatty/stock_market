package pl.stock_market.modules.order.domain;

import pl.stock_market.modules.order.api.dto.OrderDto;

import java.math.BigDecimal;

public record OrderTradeDto(OrderDto source, BigDecimal quantity) {
}
