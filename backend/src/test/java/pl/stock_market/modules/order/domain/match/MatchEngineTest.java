package pl.stock_market.modules.order.domain.match;

import org.junit.jupiter.api.Test;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.domain.Order;
import pl.stock_market.modules.order.domain.OrderTradeDto;
import pl.stock_market.modules.order.domain.match.policy.MatchPolicy;
import pl.stock_market.modules.order.domain.match.policy.MatchPolicyFactory;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.stock_market.modules.order.domain.OrderRequestType.ALL_OR_NONE;
import static pl.stock_market.modules.order.domain.match.MatchEngine.match;

class MatchEngineTest {

    @Test
    public void should_match() {
        // given
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(200);
        BigDecimal requestedQuantity = BigDecimal.valueOf(5);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio, Order.OrderType.SELL),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(120), sellerPortfolio, Order.OrderType.SELL),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio, Order.OrderType.SELL)
        );
        // when
        OrderRequest request = new OrderRequest(purchasePortfolio, requestedQuantity, requestedPrice, ALL_OR_NONE);
        MatchPolicy policy = MatchPolicyFactory.create(request);
        MatchResult result = match(book, policy);

        // then
        assertThat(result.ordersToCreate()).isEmpty();
        List<OrderTradeDto> ordersResponse = result.ordersToTrade();
        assertThat(ordersResponse.size()).isEqualTo(3L);
        assertThat(ordersResponse.get(0).source().getPrice()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(ordersResponse.get(1).source().getPrice()).isEqualTo(BigDecimal.valueOf(120));
        assertThat(ordersResponse.get(2).source().getPrice()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(ordersResponse.get(0).quantity()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(ordersResponse.get(1).quantity()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(ordersResponse.get(2).quantity()).isEqualTo(BigDecimal.valueOf(1));

        assertThat(ordersResponse.get(0).source()).isEqualTo(book.get(0));
        assertThat(ordersResponse.get(1).source()).isEqualTo(book.get(1));
        assertThat(ordersResponse.get(2).source()).isEqualTo(book.get(2));
    }


    @Test
    public void should_do_nothing_if_not_find_exactly_quantity() {
        // given
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(100);
        BigDecimal requestedQuantity = BigDecimal.valueOf(6);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(1), sellerPortfolio, Order.OrderType.SELL),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(1), sellerPortfolio, Order.OrderType.SELL),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(1), sellerPortfolio, Order.OrderType.SELL)
        );
        // when
        OrderRequest request = new OrderRequest(purchasePortfolio, requestedQuantity, requestedPrice, ALL_OR_NONE);
        MatchPolicy policy = MatchPolicyFactory.create(request);
        MatchResult result = MatchEngine.match(book, policy);

        // then
        assertThat(result.ordersToCreate()).isEmpty();
        assertThat(result.ordersToTrade()).isEmpty();
    }

    @Test
    public void should_do_nothing_if_book_empty() {
        // given
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        BigDecimal requestedQuantity = BigDecimal.valueOf(2);
        BigDecimal requestedPrice = BigDecimal.valueOf(100);
        List<OrderDto> book = List.of();
        // when
        OrderRequest request = new OrderRequest(purchasePortfolio, requestedQuantity, requestedPrice, ALL_OR_NONE);
        MatchPolicy policy = MatchPolicyFactory.create(request);
        MatchResult result = MatchEngine.match(book, policy);

        // then
        assertThat(result.ordersToCreate()).isEmpty();
        assertThat(result.ordersToTrade()).isEmpty();
    }

    @Test
    public void should_not_match_if_exceed_price_limit() {
        // given
        Portfolio purchaser = new Portfolio(2L, 2L, 1L);
        Portfolio seller = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(99);
        BigDecimal requestedQuantity = BigDecimal.valueOf(5);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), seller, Order.OrderType.SELL),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(100), seller, Order.OrderType.SELL),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), seller, Order.OrderType.SELL)
        );
        // when
        OrderRequest request = new OrderRequest(purchaser, requestedQuantity, requestedPrice, ALL_OR_NONE);
        MatchPolicy policy = MatchPolicyFactory.create(request);
        MatchResult result = MatchEngine.match(book, policy);

        // then
        assertThat(result.ordersToCreate()).isEmpty();
        assertThat(result.ordersToTrade()).isEmpty();
    }

}