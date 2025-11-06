package pl.stock_market.modules.order.match;

import org.springframework.stereotype.Service;
import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {
    public OrderMatchResult matchOrder(BigDecimal requestedQuantity, BigDecimal requestedPrice, List<OrderDto> ordersToBuy) {
        // @TODO validate quantity
        return new MatchEngine(ordersToBuy).match(requestedQuantity, requestedPrice);
    }
}
