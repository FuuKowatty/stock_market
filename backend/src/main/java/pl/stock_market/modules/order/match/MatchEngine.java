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
    public static final OrderMatchResult NOT_MATCHED = new OrderMatchResult(false, Collections.emptyList());
    private final List<OrderDto> book;

    /** book should be sorted by price and then by add date (if after then closer) */
    public OrderMatchResult match(BigDecimal desiredQuantity, BigDecimal priceLimit) {
        List<OrderToBuy> matched = new ArrayList<>();
        BigDecimal accumulatedQuantity = BigDecimal.ZERO;
        BigDecimal accumulatedPrice = BigDecimal.ZERO;
        for (OrderDto o : book) {
            accumulatedQuantity = accumulatedQuantity.add(o.quantity());
            accumulatedPrice = accumulatedPrice.add(o.price());
            if (accumulatedPrice.compareTo(priceLimit) > 0) {
                return NOT_MATCHED;
            }
            if (accumulatedQuantity.compareTo(desiredQuantity) >= 0) {
                BigDecimal tooMuch = accumulatedQuantity.subtract(desiredQuantity);
                BigDecimal correctQuantity = o.quantity().subtract(tooMuch);
                matched.add(new OrderToBuy(o, correctQuantity));
                return new OrderMatchResult(true, matched);
            }
            matched.add(new OrderToBuy(o, o.quantity()));
        }
        return NOT_MATCHED;
    }



}
