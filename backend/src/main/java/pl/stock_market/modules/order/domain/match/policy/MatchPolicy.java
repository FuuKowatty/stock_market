package pl.stock_market.modules.order.domain.match.policy;

import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.match.MatchDecision;
import pl.stock_market.modules.order.domain.match.MatchEngine;
import pl.stock_market.modules.order.domain.match.MatchResult;

public interface MatchPolicy {
    MatchDecision evaluate(OrderDto order, MatchEngine.MatchAccumulator acc);
    MatchResult toResult(MatchEngine.MatchAccumulator acc);
}
