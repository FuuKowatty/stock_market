package pl.stock_market.modules.order.domain.match;

import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.OrderTradeDto;

import java.util.List;

public record MatchResult(List<OrderDto> ordersToCreate, List<OrderTradeDto> ordersToTrade) {
}
