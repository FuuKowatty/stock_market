package pl.stock_market.modules.order;

import pl.stock_market.modules.order.api.dto.OrderToBuy;

import java.util.List;

public record OrderMatchResult(boolean matched, List<OrderToBuy> orders) {}
