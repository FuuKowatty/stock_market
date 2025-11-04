package pl.stock_market.modules.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.api.dto.OrderDto;

import java.util.List;

@Component
@RequiredArgsConstructor
class QuantityOrderRequestStrategy implements OrderRequestTypeStrategy {
    private final OrderExecutionService executor;

    @Override
    public OrderRequestType getStrategy() {
        return OrderRequestType.QUANTITY;
    }

    @Override
    public void buyOrder(OrderRequest request, List<OrderDto> ordersToBuy) {
        OrderMatchResult result = executor.matchOrder(request, ordersToBuy);
        if (!result.matched()) {
            executor.createOrder(request);
            return;
        }
        executor.tradeOrder(request, result);
    }

}
