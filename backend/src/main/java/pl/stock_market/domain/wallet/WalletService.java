package pl.stock_market.domain.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.infrastructure.application.dto.WalletOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletOperationsResolver resolver;

    @Transactional
    public void afterTrade(List<WalletOperation> operations) {
        Map<Long, BigDecimal> walletIdByMoneyChange = resolver.resolve(operations);
        List<Wallet> wallets = findWalletsByIds(walletIdByMoneyChange.keySet());
        for (Wallet wallet : wallets) {
            BigDecimal moneyChange = walletIdByMoneyChange.get(wallet.getId());
            wallet.updateMoney(moneyChange);
        }
    }

    private Wallet findById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(IllegalStateException::new);
    }

    private List<Wallet> findWalletsByIds(Set<Long> walletIds) {
        return walletRepository.findByIdIn(walletIds);
    }

    private boolean isWalletBelongsToUser(Long walletId, Long userId) {
        return walletRepository.existsByIdAndUserId(walletId, userId);
    }

}
