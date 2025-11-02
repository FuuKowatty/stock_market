package pl.stock_market.domain.order.match;

import org.springframework.stereotype.Service;
import pl.stock_market.domain.order.dto.OrderToBuy;
import pl.stock_market.domain.order.dto.OrderRequest;
import pl.stock_market.infrastructure.application.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MatchService {
    public List<OrderToBuy> matchOrder(OrderRequest orderRequest, List<OrderDto> ordersToBuy) {
        BigDecimal quantity = BigDecimal.valueOf(orderRequest.requestedQuantity());
        MatchEngine.MatchResult result = new MatchEngine(ordersToBuy).match(quantity);
        if (result == MatchEngine.MatchResult.NotMatched) {
            // some crazy exception
        }
        return result.orders();
    }
}
