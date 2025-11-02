package pl.stock_market.domain.wallet;

import pl.stock_market.infrastructure.application.dto.WalletOperation;

import java.util.List;

public interface WalletFacade {
    void doOperations(List<WalletOperation> operations);
}
