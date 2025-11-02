package pl.stock_market.domain.holder;

import java.math.BigDecimal;
import java.util.Map;

public interface HolderFacade {
    void updatePortfolio(Map<String, BigDecimal> holderQuantities);
}
