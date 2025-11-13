package pl.stock_market.modules.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LimitOrderStrategy implements  OrderRequestTypeStrategy {
    private final OrderExecutionService executor;

    @Override
    public OrderRequestType getStrategy() {
        return OrderRequestType.LIMIT_ORDER;
    }

    @Override
    public void buyOrder(OrderRequest request, List<OrderDto> ordersToBuy) {
        OrderMatchResult result = executor.matchOrder(request, ordersToBuy);
        if (!result.matched()) {
            executor.createOrder(request);
        }
        executor.tradeOrder(request, result);
    }

}
