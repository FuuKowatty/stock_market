package pl.stock_market.modules.order.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stock_market.modules.order.api.dto.OrderToBuy;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.modules.order.api.dto.TradeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    public TradeDto trade(Portfolio purchaser, List<OrderToBuy> orders) {
        TradeCalculator calculator = new TradeCalculator(purchaser);
        for (OrderToBuy order : orders) {
            calculator.processTrade(order);
        }
        return new TradeDto(calculator.getFinalPortfolioQuantityChange(), calculator.getFinalWalletOperations());
    }

}