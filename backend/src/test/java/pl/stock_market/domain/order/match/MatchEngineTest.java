package pl.stock_market.domain.order.match;

import org.junit.jupiter.api.Test;
import pl.stock_market.domain.order.dto.OrderToBuy;
import pl.stock_market.infrastructure.application.dto.OrderDto;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MatchEngineTest {

    @Test
    public void should_place_bid() {
        // given
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal askQuantity = BigDecimal.valueOf(2);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3) , BigDecimal.valueOf(120), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio)
        );
        // when
        MatchEngine.MatchResult result = new MatchEngine(book).match(askQuantity);

        // then
        List<OrderToBuy> ordersResponse = result.orders();
        assertThat(ordersResponse.size()).isEqualTo(2);
        assertThat(ordersResponse.get(0).order().price()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(ordersResponse.get(1).order().price()).isEqualTo(BigDecimal.valueOf(120));
        assertThat(ordersResponse.get(0).neededQuantity()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(ordersResponse.get(1).neededQuantity()).isEqualTo(BigDecimal.valueOf(1));

        assertThat(ordersResponse.get(0).order()).isEqualTo(book.get(0));
        assertThat(ordersResponse.get(1).order()).isEqualTo(book.get(1));
    }


    @Test
    public void should_not_match() {
        // given
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        BigDecimal askQuantity = BigDecimal.valueOf(6);
        List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(100), sellerPortfolio),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(121), sellerPortfolio),
                new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(200), sellerPortfolio)
        );
        // when
        MatchEngine.MatchResult result = new MatchEngine(book).match(askQuantity);

        // then
        assertThat(result.orders().size()).isEqualTo(0);
        assertThat(result).isEqualTo(MatchEngine.MatchResult.NotMatched);
    }

    @Test
    public void should_not_match_empty_book() {
        // given
        BigDecimal askQuantity = BigDecimal.valueOf(2);
        List<OrderDto> book = List.of();
        // when
        MatchEngine.MatchResult result = new MatchEngine(book).match(askQuantity);

        // then
        assertThat(result.orders().size()).isEqualTo(0);
        assertThat(result).isEqualTo(MatchEngine.MatchResult.NotMatched);
    }

}