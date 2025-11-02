package pl.stock_market.infrastructure.application.dto;

import java.math.BigDecimal;

public record WalletOperation(Long walletId, BigDecimal amount, Direction direction) {
    public enum Direction {
        DEPOSIT,
        WITHDRAWAL
    }

    public static WalletOperation withdraw(Long walletId, BigDecimal amount) {
        return new WalletOperation(walletId, amount, Direction.WITHDRAWAL);
    }

    public static WalletOperation deposit(Long walletId, BigDecimal amount) {
        return new WalletOperation(walletId, amount, Direction.DEPOSIT);
    }
}
