package pl.stock_market.modules.order.domain.trade;

import pl.stock_market.modules.shared.dto.Portfolio;
import pl.stock_market.modules.order.api.dto.TradeDto;
import pl.stock_market.modules.order.domain.OrderTradeDto;

import java.util.List;

public interface TradeService {
    TradeDto trade(Portfolio purchaser, List<OrderTradeDto> orders);
}
