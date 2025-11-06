package pl.stock_market.modules.order.match;

import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public interface MatchService {
    OrderMatchResult matchOrder(BigDecimal requestedQuantity, BigDecimal bigDecimal, List<OrderDto> ordersToBuy);
}
