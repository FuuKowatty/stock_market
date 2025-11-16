package pl.stock_market.modules.order.domain.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stock_market.modules.shared.dto.Portfolio;
import pl.stock_market.modules.order.api.dto.TradeDto;
import pl.stock_market.modules.order.domain.OrderTradeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    public TradeDto trade(Portfolio purchaser, List<OrderTradeDto> orders) {
        TradeCalculator calculator = new TradeCalculator(purchaser);
        for (OrderTradeDto order : orders) {
            calculator.processTrade(order);
        }
        return new TradeDto(calculator.getFinalPortfolioQuantityChange(), calculator.getFinalWalletOperations());
    }

}