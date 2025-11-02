package pl.stock_market.infrastructure.application.dto;

import java.math.BigDecimal;

public record OrderDto(Long id, BigDecimal quantity, BigDecimal price, Portfolio portfolio) {
}
