package pl.stock_market.modules.order.match;

import org.junit.jupiter.api.Test;
import pl.stock_market.modules.order.OrderMatchResult;
import pl.stock_market.modules.order.api.dto.OrderToBuy;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MatchEngineTest {

    @Test
    public void should_match() {
        // given
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(420);
        BigDecimal askQuantity = BigDecimal.valueOf(5);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3) , BigDecimal.valueOf(120), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio)
        );
        // when
        OrderMatchResult result = new MatchEngine(book).match(askQuantity, requestedPrice);

        // then
        List<OrderToBuy> ordersResponse = result.orders();
        assertThat(ordersResponse.size()).isEqualTo(3L);
        assertThat(ordersResponse.get(0).order().price()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(ordersResponse.get(1).order().price()).isEqualTo(BigDecimal.valueOf(120));
        assertThat(ordersResponse.get(2).order().price()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(ordersResponse.get(0).neededQuantity()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(ordersResponse.get(1).neededQuantity()).isEqualTo(BigDecimal.valueOf(3));
        assertThat(ordersResponse.get(2).neededQuantity()).isEqualTo(BigDecimal.valueOf(1));

        assertThat(ordersResponse.get(0).order()).isEqualTo(book.get(0));
        assertThat(ordersResponse.get(1).order()).isEqualTo(book.get(1));
        assertThat(ordersResponse.get(2).order()).isEqualTo(book.get(2));
    }


    @Test
    public void should_not_match() {
        // given
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(100);
        BigDecimal askQuantity = BigDecimal.valueOf(6);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(121), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio)
        );
        // when
        OrderMatchResult result = new MatchEngine(book).match(askQuantity, requestedPrice);

        // then
        assertThat(result.orders().size()).isEqualTo(0);
        assertThat(result.matched()).isFalse();
    }

    @Test
    public void should_not_match_empty_book() {
        // given
        BigDecimal askQuantity = BigDecimal.valueOf(2);
        BigDecimal requestedPrice = BigDecimal.valueOf(100);
        List<OrderDto> book = List.of();
        // when
        OrderMatchResult result = new MatchEngine(book).match(askQuantity, requestedPrice);

        // then
        assertThat(result.orders().size()).isEqualTo(0);
        assertThat(result.matched()).isFalse();
    }

    @Test
    public void should_not_match_if_exceed_price_limit() {
        // given
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal requestedPrice = BigDecimal.valueOf(399);
        BigDecimal askQuantity = BigDecimal.valueOf(5);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio)
        );
        // when
        OrderMatchResult result = new MatchEngine(book).match(askQuantity, requestedPrice);

        // then
        assertThat(result.orders().size()).isEqualTo(0);
        assertThat(result.matched()).isFalse();
    }

    // add tests for money limit

}