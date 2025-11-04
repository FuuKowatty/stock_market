package pl.stock_market.modules.holder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
class HolderFacadeImpl implements HolderFacade {

    private final HolderService holderService;

    public void updatePortfolio(Map<String, BigDecimal> holderQuantities) {
        holderService.updatePortfolio(holderQuantities);
    }

}
