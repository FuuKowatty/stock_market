package pl.stock_market.domain.order.match;

import lombok.RequiredArgsConstructor;
import pl.stock_market.domain.order.dto.OrderToBuy;
import pl.stock_market.infrastructure.application.dto.OrderDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class MatchEngine {
    private final List<OrderDto> book;

    /** book should be sorted by price and then by add date (if after then closer) */
    public MatchResult match(BigDecimal desiredQuantity) {
        List<OrderToBuy> matched = new ArrayList<>();
        BigDecimal accumulatedQuantity = BigDecimal.ZERO;
        for (OrderDto o : book) {
            accumulatedQuantity = accumulatedQuantity.add(o.quantity());
            if (accumulatedQuantity.compareTo(desiredQuantity) >= 0) {
                BigDecimal tooMuch = accumulatedQuantity.subtract(desiredQuantity);
                BigDecimal correctQuantity = o.quantity().subtract(tooMuch);
                matched.add(new OrderToBuy(o, correctQuantity));
                return new MatchResult(List.copyOf(matched));
            }
            matched.add(new OrderToBuy(o, o.quantity()));
        }
        return MatchResult.NotMatched;
    }

    record MatchResult(List<OrderToBuy> orders) {
        static final MatchResult NotMatched = new MatchResult(List.of());
    }

}
