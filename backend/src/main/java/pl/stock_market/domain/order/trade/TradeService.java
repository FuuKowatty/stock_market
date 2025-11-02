package pl.stock_market.domain.order.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stock_market.domain.order.dto.OrderToBuy;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.infrastructure.application.dto.TradeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    public TradeDto trade(Portfolio purchaser, List<OrderToBuy> orders) {
        TradeCalculator calculator = new TradeCalculator(purchaser);
        for (OrderToBuy order : orders) {
            calculator.processTrade(order);
        }
        return new TradeDto(calculator.getFinalPortfolioQuantityChange(), calculator.getFinalWalletOperations());
    }

}