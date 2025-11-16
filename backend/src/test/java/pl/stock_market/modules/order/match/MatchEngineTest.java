package pl.stock_market.modules.order.match;

import org.junit.jupiter.api.Test;
import pl.stock_market.infrastructure.application.dto.Portfolio;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.domain.OrderTradeDto;
import pl.stock_market.modules.order.domain.match.MatchEngine;
import pl.stock_market.modules.order.domain.match.MatchResult;
import pl.stock_market.modules.order.domain.match.policy.MatchPolicy;
import pl.stock_market.modules.order.domain.match.policy.MatchPolicyFactory;

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
        BigDecimal requestedPrice = BigDecimal.valueOf(660);
        BigDecimal requestedQuantity = BigDecimal.valueOf(5);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(120), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio)
        );
        // when
        OrderRequest request = new OrderRequest(purchasePortfolio, requestedQuantity, requestedPrice, ALL_OR_NONE);
        MatchPolicy policy = MatchPolicyFactory.create(request);
        MatchResult result = match(book, policy);

        // then
        assertThat(result.ordersToCreate()).isEmpty();
        List<OrderTradeDto> ordersResponse = result.ordersToTrade();
        assertThat(ordersResponse.size()).isEqualTo(3L);
        assertThat(ordersResponse.get(0).source().price()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(ordersResponse.get(1).source().price()).isEqualTo(BigDecimal.valueOf(120));
        assertThat(ordersResponse.get(2).source().price()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(ordersResponse.get(0).quantity()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(ordersResponse.get(1).quantity()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(ordersResponse.get(2).quantity()).isEqualTo(BigDecimal.valueOf(1));

        assertThat(ordersResponse.get(0).source()).isEqualTo(book.get(0));
        assertThat(ordersResponse.get(1).source()).isEqualTo(book.get(1));
        assertThat(ordersResponse.get(2).source()).isEqualTo(book.get(2));
    }


    @Test
    public void should_do_nothing_if_not_find_quantity() {
        // given
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(100);
        BigDecimal requestedQuantity = BigDecimal.valueOf(6);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(1), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(1), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(1), sellerPortfolio)
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
        BigDecimal requestedPrice = BigDecimal.valueOf(399);
        BigDecimal requestedQuantity = BigDecimal.valueOf(5);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), seller),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(100), seller),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), seller)
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