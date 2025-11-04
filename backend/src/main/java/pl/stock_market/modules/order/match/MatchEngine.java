package pl.stock_market.modules.order.match;

import lombok.RequiredArgsConstructor;
import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderToBuy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
class MatchEngine {
    private final List<OrderDto> book;

    /** book should be sorted by price and then by add date (if after then closer) */
    public OrderMatchResult match(BigDecimal desiredQuantity) {
        List<OrderToBuy> matched = new ArrayList<>();
        BigDecimal accumulatedQuantity = BigDecimal.ZERO;
        for (OrderDto o : book) {
            accumulatedQuantity = accumulatedQuantity.add(o.quantity());
            if (accumulatedQuantity.compareTo(desiredQuantity) >= 0) {
                BigDecimal tooMuch = accumulatedQuantity.subtract(desiredQuantity);
                BigDecimal correctQuantity = o.quantity().subtract(tooMuch);
                matched.add(new OrderToBuy(o, correctQuantity));
                return new OrderMatchResult(true, matched);
            }
            matched.add(new OrderToBuy(o, o.quantity()));
        }
        return new OrderMatchResult(false, Collections.emptyList());
    }



}
