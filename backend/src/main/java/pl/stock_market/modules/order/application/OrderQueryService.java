package pl.stock_market.modules.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.modules.order.OrderMapper;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.Order;
import pl.stock_market.modules.order.domain.OrderRepository;

import java.util.List;

import static pl.stock_market.modules.order.domain.Order.OrderType.SELL;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {
    private final OrderRepository repository;

    public List<OrderDto> getOrdersToBuy(Long stockId) {
        List<Order> ordersToBuy = repository.findByPortfolioStockIdAndTypeAndAssetsActivatedTrue(stockId, SELL);
        return ordersToBuy.stream()
                .map(OrderMapper::mapToDto)
                .toList();
    }

}
