package pl.stock_market.modules.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.application.OrderFacade;
import pl.stock_market.modules.order.application.OrderQueryService;

import java.util.List;

@Service
@RequiredArgsConstructor
class OrderFacadeImpl implements OrderFacade {

    private final OrderRequestTypeStrategyRegistry registry;
    private final OrderQueryService query;

    @Transactional
    public void buy(OrderRequest orderRequest) {
        List<OrderDto> ordersToBuy = query.getOrdersToBuy(orderRequest.portfolio().stockId());
        OrderRequestTypeStrategy strategy = registry.getStrategy(orderRequest.type());
        strategy.buyOrder(orderRequest, ordersToBuy);
    }

}
