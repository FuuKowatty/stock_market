package pl.stock_market.modules.order.domain.trade;

import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.shared.dto.Portfolio;
import pl.stock_market.modules.shared.dto.WalletOperation;
import pl.stock_market.modules.order.domain.OrderTradeDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeCalculator {
    private final Map<String, BigDecimal> portfolioQuantityChange = new HashMap<>();
    private final List<WalletOperation> walletOperations = new ArrayList<>();
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
    private final Portfolio purchaser;
    private boolean finalized = false;

    TradeCalculator(Portfolio portfolio) {
        this.purchaser = portfolio;
    }

    void processTrade(OrderTradeDto orderToBuy) {
        OrderDto order = orderToBuy.source();
        BigDecimal neededQuantity = orderToBuy.quantity();
        updatePortfolio(order, neededQuantity);
        processPayment(order, neededQuantity);
    }

    void updatePortfolio(OrderDto order, BigDecimal neededQuantity) {
        portfolioQuantityChange.merge(order.getPortfolio().getId(), neededQuantity.negate(), BigDecimal::add);
        portfolioQuantityChange.merge(purchaser.getId(), neededQuantity, BigDecimal::add);
    }

    void processPayment(OrderDto order, BigDecimal neededQuantity) {
        BigDecimal tradeValue = order.getPrice().multiply(neededQuantity);
        walletOperations.add(WalletOperation.deposit(order.getPortfolio().walletId(), tradeValue));
        totalPurchaseAmount = totalPurchaseAmount.add(tradeValue);
    }

    Map<String, BigDecimal> getFinalPortfolioQuantityChange() {
        return Map.copyOf((portfolioQuantityChange));
    }

    // @TODO think if i can handle it better there is "final" other getter
    List<WalletOperation> getFinalWalletOperations() {
        if (finalized) {
            throw new IllegalStateException("Final operations have already been retrieved.");
        }
        finalized = true;
        walletOperations.add(WalletOperation.withdraw(purchaser.walletId(), totalPurchaseAmount));
        return List.copyOf(walletOperations);
    }
}
