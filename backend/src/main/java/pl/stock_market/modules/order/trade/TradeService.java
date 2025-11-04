package pl.stock_market.modules.order.trade;

import pl.stock_market.modules.order.api.dto.OrderToBuy;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.modules.order.api.dto.TradeDto;

import java.util.List;

public interface TradeService {
    TradeDto trade(Portfolio purchaser, List<OrderToBuy> orders);
}
