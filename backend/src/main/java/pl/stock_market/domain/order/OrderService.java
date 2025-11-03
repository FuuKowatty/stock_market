package pl.stock_market.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.domain.holder.HolderFacade;
import pl.stock_market.domain.order.dto.OrderRequest;
import pl.stock_market.domain.order.dto.OrderToBuy;
import pl.stock_market.domain.order.match.MatchService;
import pl.stock_market.domain.order.trade.TradeService;
import pl.stock_market.domain.wallet.WalletFacade;
import pl.stock_market.infrastructure.application.dto.OrderDto;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.infrastructure.application.dto.TradeDto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class OrderService {

    private final OrderRepository orderRepository;
    private final MatchService matchService;
    private final WalletFacade walletFacade;
    private final TradeService tradeService;
    private final HolderFacade holderFacade;

    public List<OrderDto> getOrdersToBuy(Long stockId) {
        List<Order> ordersToBuy = orderRepository.findByPortfolioStockIdAndAssetsActivatedTrue(stockId);
        return ordersToBuy.stream()
                .map(OrderMapper::mapToDto)
                .toList();
    }

    @Transactional
    public void buy(OrderRequest orderRequest) {
        List<OrderDto> ordersToBuy = getOrdersToBuy(orderRequest.stockId());
        List<OrderToBuy> matchedOrders = matchService.matchOrder(orderRequest, ordersToBuy);
        Portfolio purchaser = new Portfolio(orderRequest.userId(), orderRequest.walletId(), orderRequest.stockId());
        TradeDto tradeResult = tradeService.trade(purchaser, matchedOrders);
        updateActivateStatus(matchedOrders);
        walletFacade.doOperations(tradeResult.walletChange());
        holderFacade.updatePortfolio(tradeResult.portfolioQuantityChange());
    }

    private void updateActivateStatus(List<OrderToBuy> matchedOrders) {
        Set<Long> orderIds = matchedOrders.stream()
                .map(mo -> mo.order().id())
                .collect(Collectors.toSet());
        Map<Long, Order> ordersById = orderRepository.findByIdIn(orderIds).stream()
                .collect(Collectors.toMap(Order::getId, order -> order));
        for (OrderToBuy match : matchedOrders) {
            Order orderEntity = ordersById.get(match.order().id());
            orderEntity.addQuantity(match.neededQuantity().negate());
        }
    }

    static class OrderMapper {
        static OrderDto mapToDto(Order order) {
            return new OrderDto(
                    order.getId(),
                    order.getQuantity(),
                    order.getPrice(),
                    order.getPortfolio()
            );
        }
    }
}
