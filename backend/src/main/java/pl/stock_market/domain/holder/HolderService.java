package pl.stock_market.domain.holder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class HolderService {

    private final HolderRepository holderRepository;

    @Transactional
    public void updatePortfolio(Map<String, BigDecimal> holderQuantities) {
        Set<Portfolio> portfolios = holderQuantities.keySet().stream()
                .map(Portfolio::fromId)
                .collect(Collectors.toSet());
        List<Holder> holders = holderRepository.findByPortfolioIn(portfolios);
        for (Holder holder : holders) {
            String portfolioId = holder.getPortfolio().getId();
            BigDecimal quantityToUpdate = holderQuantities.get(portfolioId);
            holder.addQuantity(quantityToUpdate);
        }
        holderRepository.saveAll(holders);
    }

}
