package pl.stock_market.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByIdIn(Set<Long> longs);
    List<Order> findByPortfolioStockIdAndAssetsActivatedTrue(Long stockId);
}
