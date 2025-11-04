package pl.stock_market.modules.order.trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import pl.stock_market.domain.exchange.order.trading.TradeCalculator;
//import pl.stock_market.infrastructure.application.dto.HolderDto;
import pl.stock_market.modules.order.api.dto.OrderToBuy;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.infrastructure.application.dto.WalletOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TradeCalculatorTest {
    private TradeCalculator calculator;
    private Portfolio sellerPortfolio;
    private Portfolio purchaserPortfolio;

    @BeforeEach
    void setUp() {
        purchaserPortfolio = new Portfolio(1L, 1L, 1L);
        sellerPortfolio = new Portfolio(2L, 2L, 1L);
        calculator = new TradeCalculator(purchaserPortfolio);
    }

    @Test
    void shouldProcessMultipleTrades() {
        // given
        OrderDto order1 = new OrderDto(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(10), sellerPortfolio);
        OrderDto order2 = new OrderDto(2L, BigDecimal.valueOf(80), BigDecimal.valueOf(15), sellerPortfolio);
        OrderToBuy orderToBuy1 = new OrderToBuy(order1, BigDecimal.valueOf(100));
        OrderToBuy orderToBuy2 = new OrderToBuy(order2, BigDecimal.valueOf(20));

        // when
        calculator.processTrade(orderToBuy1);
        calculator.processTrade(orderToBuy2);

        // then
        Map<String, BigDecimal> orderChanges = calculator.getFinalPortfolioQuantityChange();
        assertThat(orderChanges.get("u1:w1:s1")).isEqualTo(BigDecimal.valueOf(120));
        assertThat(orderChanges.get("u2:w2:s1")).isEqualTo(BigDecimal.valueOf(-120));

        List<WalletOperation> walletOps = calculator.getFinalWalletOperations();
        assertThat(walletOps).hasSize(3);
        assertThat(walletOps.get(0).amount()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(walletOps.get(0).direction()).isEqualTo(WalletOperation.Direction.DEPOSIT);
        assertThat(walletOps.get(0).walletId()).isEqualTo(sellerPortfolio.walletId());

        assertThat(walletOps.get(1).amount()).isEqualTo(BigDecimal.valueOf(300));
        assertThat(walletOps.get(1).direction()).isEqualTo(WalletOperation.Direction.DEPOSIT);
        assertThat(walletOps.get(1).walletId()).isEqualTo(sellerPortfolio.walletId());

        assertThat(walletOps.get(2).amount()).isEqualTo(BigDecimal.valueOf(1300));
        assertThat(walletOps.get(2).direction()).isEqualTo(WalletOperation.Direction.WITHDRAWAL);
        assertThat(walletOps.get(2).walletId()).isEqualTo(purchaserPortfolio.walletId());
    }

    @Test
    void shouldThrowExceptionIfMutateResult() {
        // given
        OrderDto order1 = new OrderDto(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(10), sellerPortfolio);
        OrderDto order2 = new OrderDto(2L, BigDecimal.valueOf(80), BigDecimal.valueOf(15), sellerPortfolio);
        OrderToBuy orderToBuy1 = new OrderToBuy(order1, BigDecimal.valueOf(100));

        // when
        calculator.processTrade(orderToBuy1);

        // then
        Map<String, BigDecimal> holderChanges = calculator.getFinalPortfolioQuantityChange();
        assertThatThrownBy(() -> holderChanges.put("u1:w1:s1", BigDecimal.ONE))
                .isInstanceOf(UnsupportedOperationException.class);

        List<WalletOperation> walletOps = calculator.getFinalWalletOperations();
        assertThatThrownBy(() -> walletOps.add(WalletOperation.deposit(999L, BigDecimal.ONE)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}