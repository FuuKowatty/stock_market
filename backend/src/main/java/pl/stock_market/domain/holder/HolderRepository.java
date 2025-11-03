package pl.stock_market.domain.holder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.util.List;
import java.util.Set;

@Repository
interface HolderRepository extends JpaRepository<Holder, Long> {
    @Query("SELECT h FROM Holder h WHERE h.portfolio IN :portfolios")
    List<Holder> findByPortfolioIn(Set<Portfolio> portfolios);
}
