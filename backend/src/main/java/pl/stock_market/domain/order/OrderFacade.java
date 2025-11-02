package pl.stock_market.domain.order;

import java.math.BigDecimal;
import java.util.Map;

public interface OrderFacade {
    void afterTrade(Map<Long, BigDecimal> orderQuantities);
}
