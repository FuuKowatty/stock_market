package pl.stock_market.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.stock_market.domain.holder.HolderFacade;
import pl.stock_market.domain.order.dto.OrderToBuy;
import pl.stock_market.domain.order.dto.OrderRequest;
import pl.stock_market.domain.order.match.MatchService;
import pl.stock_market.domain.order.trade.TradeService;
import pl.stock_market.domain.wallet.WalletFacade;
import pl.stock_market.infrastructure.application.dto.OrderDto;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.infrastructure.application.dto.TradeDto;

import java.util.*;

@Service
@RequiredArgsConstructor
class OrderService {

    private final OrderRepository orderRepository;
    private final MatchService matchService;
    private final WalletFacade walletFacade;
    private final TradeService tradeService;
    private final HolderFacade holderFacade;

    public List<OrderDto> getOrdersToBuy(Long stockId) {
        List<Order> ordersToBuy = orderRepository.findByPortfolioStockIdAndTypeAndActiveTrue(stockId, Order.OrderType.BUY);
        return ordersToBuy.stream()
                .map(OrderMapper::mapToDto)
                .toList();
    }

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
        Set<Long> orderIdsToInactive = new HashSet<>();
        for (OrderToBuy orderToBuy : matchedOrders) {
            if (orderToBuy.neededQuantity().compareTo(orderToBuy.order().quantity()) > 0) {
                orderIdsToInactive.add(orderToBuy.order().id());
            }
        }
        setInactive(orderIdsToInactive);
    }

    private void setInactive(Set<Long> orderIdsToInactive) {
        for (Order order : orderRepository.findByIdIn(orderIdsToInactive)) {
            order.setActive(false);
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
