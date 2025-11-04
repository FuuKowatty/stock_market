package pl.stock_market.modules.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByIdIn(Set<Long> longs);
    List<Order> findByPortfolioStockIdAndTypeAndAssetsActivatedTrue(Long stockId, Order.OrderType type);
}
