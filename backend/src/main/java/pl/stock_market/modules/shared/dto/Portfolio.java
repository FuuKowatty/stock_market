package pl.stock_market.modules.shared.dto;

import jakarta.persistence.Embeddable;

@Embeddable
public record Portfolio(Long userId, Long walletId, Long stockId) {
    public String getId() {
        return String.format("u%s:w%s:s%s", userId, walletId, stockId);
    }

    public static Portfolio fromId(String id) {
        String[] parts = id.split(":");
        Long userId = Long.parseLong(parts[0].substring(1));
        Long walletId = Long.parseLong(parts[1].substring(1));
        Long stockId = Long.parseLong(parts[2].substring(1));
        return new Portfolio(userId, walletId, stockId);
    }
}
