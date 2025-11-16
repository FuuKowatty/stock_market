package pl.stock_market.modules.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.modules.holder.HolderFacade;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.api.dto.TradeDto;
import pl.stock_market.modules.order.domain.match.MatchResult;
import pl.stock_market.modules.order.trade.TradeService;
import pl.stock_market.modules.wallet.WalletFacade;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.stock_market.modules.order.domain.match.MatchEngine.match;
import static pl.stock_market.modules.order.domain.match.policy.MatchPolicyFactory.create;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderQueryService queryService;
    private final TradeService tradeService;
    private final OrderRepository orderRepository;
    private final HolderFacade holderFacade;
    private final WalletFacade walletFacade;

    @Transactional
    public void buy(OrderRequest orderRequest) {
        var ordersToBuy = queryService.getOrdersToBuy(orderRequest.portfolio().stockId());
        MatchResult match = match(ordersToBuy, create(orderRequest));
        if (!match.ordersToCreate().isEmpty()) {
            match.ordersToCreate().forEach(dto -> createOrder(orderRequest, dto));
        }
        if (!match.ordersToTrade().isEmpty()) {
            tradeOrder(orderRequest, match.ordersToTrade());
        }
    }

    void tradeOrder(OrderRequest orderRequest, List<OrderTradeDto> orders) {
        TradeDto tradeResult = tradeService.trade(orderRequest.portfolio(), orders);
        updateActivateStatus(orders);
        walletFacade.doOperations(tradeResult.walletChange());
        holderFacade.updatePortfolio(tradeResult.portfolioQuantityChange());
    }

    private void updateActivateStatus(List<OrderTradeDto> orders) {
        Set<Long> orderIds = orders.stream()
                .map(order -> order.source().id())
                .collect(Collectors.toSet());
        Map<Long, Order> ordersById = orderRepository.findByIdIn(orderIds).stream()
                .collect(Collectors.toMap(Order::getId, order -> order));
        for (OrderTradeDto order : orders) {
            Order orderEntity = ordersById.get(order.source().id());
            orderEntity.addQuantity(order.quantity().negate());
        }
    }

    private Long createOrder(OrderRequest request, OrderDto dto) {
        Order order = new Order(dto.price(), request.portfolio(), Order.OrderType.BUY, new OrderAssets(dto.quantity(), true));
        return orderRepository.save(order).getId();
    }


}
