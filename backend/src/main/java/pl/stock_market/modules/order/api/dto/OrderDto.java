package pl.stock_market.modules.order.api.dto;

import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

public record OrderDto(Long id, BigDecimal quantity, BigDecimal price, Portfolio portfolio) {
}
