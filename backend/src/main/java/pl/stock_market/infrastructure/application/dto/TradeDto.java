package pl.stock_market.infrastructure.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record TradeDto(Map<String, BigDecimal> portfolioQuantityChange, List<WalletOperation> walletChange) {
}
