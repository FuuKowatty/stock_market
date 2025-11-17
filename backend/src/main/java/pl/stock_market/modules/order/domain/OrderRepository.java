package pl.stock_market.modules.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByIdIn(Set<Long> longs);
    @Query("""
            SELECT o FROM Order o
            WHERE o.portfolio.stockId = :stockId AND o.type = :type AND o.assets.activated = true
            ORDER BY o.price ASC, o.created ASC
            """)
    List<Order> findOrders(@Param("stockId") Long stockId, @Param("type") Order.OrderType type);
}
