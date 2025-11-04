package pl.stock_market.modules.order.api.dto;

import pl.stock_market.modules.order.domain.OrderRequestType;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

// @TODO add serialization for Bigdecimal
public record OrderRequest(Portfolio portfolio, BigDecimal requestedQuantity, BigDecimal requestedPrice, OrderRequestType type) {
}
