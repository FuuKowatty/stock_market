package pl.stock_market.modules.wallet;

import org.springframework.stereotype.Component;
import pl.stock_market.infrastructure.application.dto.WalletOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WalletOperationsResolver {
    public Map<Long, BigDecimal> resolve(List<WalletOperation> operations) {
        return operations.stream()
                .collect(Collectors.groupingBy(
                        WalletOperation::walletId,
                        Collectors.mapping(
                                op -> op.direction() == WalletOperation.Direction.DEPOSIT
                                        ? op.amount()
                                        : op.amount().negate(),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }

}
