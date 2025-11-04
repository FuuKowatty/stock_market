package pl.stock_market.modules.order.api.dto;

import pl.stock_market.infrastructure.application.dto.WalletOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record TradeDto(Map<String, BigDecimal> portfolioQuantityChange, List<WalletOperation> walletChange) {
}
