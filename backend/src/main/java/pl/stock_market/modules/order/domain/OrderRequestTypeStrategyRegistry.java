package pl.stock_market.modules.order.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class OrderRequestTypeStrategyRegistry {
    private final Map<OrderRequestType, OrderRequestTypeStrategy> strategyMap;

    public OrderRequestTypeStrategyRegistry(List<OrderRequestTypeStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        OrderRequestTypeStrategy::getStrategy,
                        strategy -> strategy
                ));
    }

    public OrderRequestTypeStrategy getStrategy(OrderRequestType type) {
        OrderRequestTypeStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Not found startegy of type " + type);
        }
        return strategy;
    }

}