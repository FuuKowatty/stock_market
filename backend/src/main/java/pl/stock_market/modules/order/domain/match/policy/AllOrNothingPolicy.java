package pl.stock_market.modules.order.domain.match.policy;

import lombok.RequiredArgsConstructor;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.match.MatchDecision;
import pl.stock_market.modules.order.domain.match.MatchEngine;
import pl.stock_market.modules.order.domain.match.MatchResult;

import java.math.BigDecimal;
import java.util.Collections;

@RequiredArgsConstructor
class AllOrNothingPolicy implements MatchPolicy {

    private final BigDecimal requestedQuantity;
    private final BigDecimal priceLimit;

    @Override
    public MatchDecision evaluate(OrderDto order, MatchEngine.MatchAccumulator acc) {
        var needed = requestedQuantity.subtract(acc.getCurrentQuantity());
        if (canFillRemaining(order, needed)) {
            var cost = needed.multiply(order.price());
            return exceedsBudget(acc, cost) ? MatchDecision.skip() : MatchDecision.takePartial(needed);
        }
        var cost = order.quantity().multiply(order.price());
        return exceedsBudget(acc, cost) ? MatchDecision.skip() : MatchDecision.takeFull(order.quantity());
    }

    private boolean canFillRemaining(OrderDto order, BigDecimal needed) {
        return order.quantity().compareTo(needed) >= 0;
    }

    private boolean exceedsBudget(MatchEngine.MatchAccumulator acc, BigDecimal additionalCost) {
        return acc.getCurrentPrice().add(additionalCost).compareTo(priceLimit) > 0;
    }

    @Override
    public MatchResult toResult(MatchEngine.MatchAccumulator acc) {
        if (acc.getCurrentQuantity().compareTo(requestedQuantity) < 0) {
            return new MatchResult(Collections.emptyList(), Collections.emptyList());
        }
        return new MatchResult(Collections.emptyList(), acc.getMatchedOrders());
    }
}
