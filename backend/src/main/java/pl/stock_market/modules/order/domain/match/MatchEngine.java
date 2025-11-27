package pl.stock_market.modules.order.domain.match;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.OrderTradeDto;
import pl.stock_market.modules.order.domain.match.policy.MatchPolicy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MatchEngine {

    /** book should be sorted by price and then by add date (if after then closer)*/
    public static MatchResult match(List<OrderDto> book, MatchPolicy policy) {
        MatchAccumulator acc = new MatchAccumulator();
        for (OrderDto order : book) {
            MatchDecision decision = policy.evaluate(order, acc);
            if (decision != MatchDecision.skip()) {
                acc.accumulate(order, decision.getQuantity());
            }
            if (decision.isStopMatching()) {
                break;
            }
        }
        return policy.toResult(acc);
    }

    @Getter
    public static class MatchAccumulator {
        List<OrderTradeDto> matchedOrders = new ArrayList<>();
        BigDecimal currentQuantity = BigDecimal.ZERO;
        BigDecimal currentPrice = BigDecimal.ZERO;

        public void accumulate(OrderDto o, BigDecimal quantityToTake) {
            this.currentQuantity = currentQuantity.add(quantityToTake);
            this.currentPrice = currentPrice.add(quantityToTake.multiply(o.getPrice()));
            this.matchedOrders.add(new OrderTradeDto(o, quantityToTake));
        }
    }
}