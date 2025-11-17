package pl.stock_market.modules.order.domain.match.policy;

import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.match.MatchDecision;
import pl.stock_market.modules.order.domain.match.MatchEngine;
import pl.stock_market.modules.order.domain.match.MatchResult;

import java.math.BigDecimal;
import java.util.Collections;

class AllOrNothingPolicy implements MatchPolicy {

    private final BigDecimal requestedQuantity;
    private final BigDecimal requestedPice;

    public AllOrNothingPolicy(BigDecimal requestedQuantity, BigDecimal requestedPice) {
        this.requestedQuantity = requestedQuantity;
        this.requestedPice = requestedPice;
    }

    @Override
    public MatchDecision evaluate(OrderDto order, MatchEngine.MatchAccumulator acc) {
        var needed = requestedQuantity.subtract(acc.getCurrentQuantity());
        if (canFillRemaining(order, needed)) {
            return exceedsBudget(order) ? MatchDecision.skip() : MatchDecision.takePartial(needed);
        }
        return exceedsBudget(order) ? MatchDecision.skip() : MatchDecision.takeFull(order.getQuantity());
    }

    private boolean exceedsBudget(OrderDto order) {
        return order.getPrice().compareTo(requestedPice) > 0;
    }

    private boolean canFillRemaining(OrderDto order, BigDecimal needed) {
        return order.getQuantity().compareTo(needed) >= 0;
    }

    @Override
    public MatchResult toResult(MatchEngine.MatchAccumulator acc) {
        if (acc.getCurrentQuantity().compareTo(requestedQuantity) < 0) {
            return new MatchResult(Collections.emptyList(), Collections.emptyList());
        }
        return new MatchResult(Collections.emptyList(), acc.getMatchedOrders());
    }
}
