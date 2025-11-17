package pl.stock_market.modules.wallet;

import pl.stock_market.modules.shared.dto.WalletOperation;

import java.util.List;

public interface WalletFacade {
    void doOperations(List<WalletOperation> operations);
}
