package pl.stock_market.domain.order.dto;

public record OrderRequest(Long userId, Long walletId, Long stockId, Long requestedQuantity) {
}
