package pl.stock_market.modules.holder;

import java.math.BigDecimal;
import java.util.Map;

public interface HolderFacade {
    void updatePortfolio(Map<String, BigDecimal> holderQuantities);
}
