package pl.stock_market.modules.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.stock_market.modules.shared.dto.WalletOperation;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WalletFacadeImpl implements WalletFacade {

    private final WalletService walletService;

    @Override
    public void doOperations(List<WalletOperation> operations) {
        walletService.afterTrade(operations);
    }

}

