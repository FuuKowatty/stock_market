package pl.stock_market.modules.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stock_market.modules.holder.HolderFacade;
import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.api.dto.OrderToBuy;
import pl.stock_market.modules.order.api.dto.TradeDto;
import pl.stock_market.modules.order.match.MatchService;
import pl.stock_market.modules.order.trade.TradeService;
import pl.stock_market.modules.wallet.WalletFacade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderExecutionService {

    private final TradeService tradeService;
    private final MatchService matchService;
    private final OrderRepository orderRepository;
    private final HolderFacade holderFacade;
    private final WalletFacade walletFacade;

    OrderMatchResult matchOrder(OrderRequest request, List<OrderDto> ordersToBuy) {
        return matchService.matchOrder(request.requestedQuantity(), request.requestedPrice(), ordersToBuy);
    }

    void tradeOrder(OrderRequest orderRequest, OrderMatchResult result) {
        List<OrderToBuy> matchedOrders = result.orders();
        TradeDto tradeResult = tradeService.trade(orderRequest.portfolio(), matchedOrders);
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

    public Long createOrder(OrderRequest request) {
        Order order = new Order(request.requestedPrice(), request.portfolio(), Order.OrderType.BUY, new OrderAssets(request.requestedQuantity(), true));
        return orderRepository.save(order).getId();
    }

}
