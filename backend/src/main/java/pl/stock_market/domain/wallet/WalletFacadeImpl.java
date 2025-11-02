package pl.stock_market.domain.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.stock_market.infrastructure.application.dto.WalletOperation;

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

