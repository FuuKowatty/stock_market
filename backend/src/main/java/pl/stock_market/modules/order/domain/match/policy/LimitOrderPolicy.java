package pl.stock_market.modules.order.domain.match.policy;

import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.Order;
import pl.stock_market.modules.order.domain.match.MatchDecision;
import pl.stock_market.modules.order.domain.match.MatchEngine;
import pl.stock_market.modules.order.domain.match.MatchResult;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static pl.stock_market.modules.order.domain.match.MatchDecision.skip;
import static pl.stock_market.modules.order.domain.match.MatchDecision.takePartial;

class LimitOrderPolicy implements MatchPolicy {
    private final BigDecimal requestedQuantity;
    private final BigDecimal requestedPriceLimit;
    private final Portfolio purchaser;
    private BigDecimal neededQuantity;


    public LimitOrderPolicy(Portfolio purchaser, BigDecimal requestedPriceLimit, BigDecimal requestedQuantity) {
        this.purchaser = purchaser;
        this.requestedPriceLimit = requestedPriceLimit;
        this.requestedQuantity = requestedQuantity;
    }

    @Override
    public MatchDecision evaluate(OrderDto order, MatchEngine.MatchAccumulator acc) {
        var needed = requestedQuantity.subtract(acc.getCurrentQuantity());
        if (canFillRemaining(order, needed)) {
            if (exceedsBudget(order)) {
                neededQuantity = needed;
                return skip();
            } else {
                return takePartial(needed);
            }
        }
        return exceedsBudget(order) ? skip() : MatchDecision.takeFull(order.getQuantity());
    }

    private boolean canFillRemaining(OrderDto order, BigDecimal needed) {
        return order.getQuantity().compareTo(needed) >= 0;
    }

    private boolean exceedsBudget(OrderDto order) {
        return order.getPrice().compareTo(requestedPriceLimit) > 0;
    }

    @Override
    public MatchResult toResult(MatchEngine.MatchAccumulator acc) {
        if (acc.getCurrentQuantity().compareTo(requestedQuantity) < 0) {
            return new MatchResult(List.of(
                    new OrderDto(neededQuantity, requestedPriceLimit, purchaser, Order.OrderType.SELL)),
                    acc.getMatchedOrders());
        }
        return new MatchResult(Collections.emptyList(), acc.getMatchedOrders());
    }

}

