package pl.stock_market.modules.order.domain;

import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.api.dto.OrderDto;

import java.util.List;

interface OrderRequestTypeStrategy {
    OrderRequestType getStrategy();
    void buyOrder(OrderRequest request, List<OrderDto> ordersToBuy);
}
