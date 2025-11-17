package pl.stock_market.modules.order.domain.match;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.domain.OrderRequestType;
import pl.stock_market.modules.order.domain.OrderTradeDto;
import pl.stock_market.modules.order.domain.match.policy.MatchPolicyFactory;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.stock_market.modules.order.domain.Order.OrderType.BUY;
import static pl.stock_market.modules.order.domain.Order.OrderType.SELL;
import static pl.stock_market.modules.order.domain.OrderRequestType.ALL_OR_NONE;
import static pl.stock_market.modules.order.domain.OrderRequestType.LIMIT_ORDER;
import static pl.stock_market.modules.order.domain.match.MatchEngine.match;

class MatchEngineTest {


    @Test
    void should_match_all_or_none() {
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        new MatchTestBuilder()
                .withBook(List.of(
                        new OrderDto(1L, BigDecimal.ONE, BigDecimal.valueOf(100), sellerPortfolio, SELL),
                        new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(120), sellerPortfolio, SELL),
                        new OrderDto(3L, BigDecimal.ONE, BigDecimal.valueOf(200), sellerPortfolio, SELL)
                ))
                .whenMatch("5", "200", ALL_OR_NONE)
                .then(result -> {
                    assertThat(result.ordersToCreate()).isEmpty();
                    List<OrderTradeDto> orderTradeDtos = result.ordersToTrade();
                    assertThat(orderTradeDtos).hasSize(3);
                    assertThat(orderTradeDtos.get(0).source().getPrice()).isEqualTo(BigDecimal.valueOf(100));
                    assertThat(orderTradeDtos.get(1).source().getPrice()).isEqualTo(BigDecimal.valueOf(120));
                    assertThat(orderTradeDtos.get(2).source().getPrice()).isEqualTo(BigDecimal.valueOf(200));
                    assertThat(orderTradeDtos.get(0).quantity()).isEqualTo(BigDecimal.valueOf(1));
                    assertThat(orderTradeDtos.get(1).quantity()).isEqualTo(BigDecimal.valueOf(3));
                    assertThat(orderTradeDtos.get(2).quantity()).isEqualTo(BigDecimal.valueOf(1));
                });
    }

    @Test
    public void should_do_nothing_if_not_find_exactly_quantity() {
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        new MatchTestBuilder()
                .withPurchaser(purchasePortfolio)
                .withBook(List.of(
                        new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(1), sellerPortfolio, SELL),
                        new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(1), sellerPortfolio, SELL),
                        new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(1), sellerPortfolio, SELL)
                ))
                .whenMatch("6", "100", ALL_OR_NONE)
                .then(result -> {
                    assertThat(result.ordersToCreate()).isEmpty();
                    assertThat(result.ordersToTrade()).isEmpty();
                });
    }

    @Test
    public void should_do_nothing_if_book_empty() {
        new MatchTestBuilder()
                .withBook(emptyList())
                .whenMatch("1", "1", ALL_OR_NONE)
                .then(result -> {
                    assertThat(result.ordersToCreate()).isEmpty();
                    assertThat(result.ordersToTrade()).isEmpty();
                });
    }

    @Test
    public void should_not_match_if_exceed_price_limit() {
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        new MatchTestBuilder()
                .withPurchaser(purchasePortfolio)
                .withBook(List.of(
                        new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(101), sellerPortfolio, SELL),
                        new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(102), sellerPortfolio, SELL),
                        new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(104), sellerPortfolio, SELL)
                ))
                .whenMatch("5", "103", ALL_OR_NONE)
                .then(result -> {
                    assertThat(result.ordersToCreate()).isEmpty();
                    assertThat(result.ordersToTrade()).isEmpty();
                });
    }


    @Test
    void should_match_limit_order() {
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        Portfolio seller2Portfolio = new Portfolio(2L, 2L, 1L);
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        new MatchTestBuilder()
                .withPurchaser(purchasePortfolio)
                .withBook(List.of(
                        new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(101), sellerPortfolio, SELL),
                        new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(102), seller2Portfolio, SELL),
                        new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(104), sellerPortfolio, SELL)
                ))
                .whenMatch("2", "101", LIMIT_ORDER)
                .then(result -> {
                    assertThat(result.ordersToTrade()).hasSize(1);
                    assertThat(result.ordersToTrade().get(0).source().getPrice()).isEqualTo(BigDecimal.valueOf(101));
                    assertThat(result.ordersToTrade().get(0).source().getQuantity()).isEqualTo(BigDecimal.valueOf(1));
                    assertThat(result.ordersToTrade().get(0).source().getType()).isEqualTo(SELL);
                    assertThat(result.ordersToTrade().get(0).source().getPortfolio()).isEqualTo(sellerPortfolio);

                    assertThat(result.ordersToCreate()).hasSize(1);
                    assertThat(result.ordersToCreate().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(101));
                    assertThat(result.ordersToCreate().get(0).getQuantity()).isEqualTo(BigDecimal.valueOf(1));
                    assertThat(result.ordersToCreate().get(0).getType()).isEqualTo(BUY);
                    assertThat(result.ordersToCreate().get(0).getPortfolio()).isEqualTo(purchasePortfolio);
                });
    }

    @Test
    public void should_create_orders_on_empty_book() {
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        new MatchTestBuilder()
                .withPurchaser(purchasePortfolio)
                .withBook(emptyList())
                .whenMatch("2", "125", LIMIT_ORDER)
                .then(result -> {
                    assertThat(result.ordersToCreate()).hasSize(1);
                    assertThat(result.ordersToCreate().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(125));
                    assertThat(result.ordersToCreate().get(0).getQuantity()).isEqualTo(BigDecimal.valueOf(2));
                    assertThat(result.ordersToCreate().get(0).getType()).isEqualTo(BUY);
                    assertThat(result.ordersToCreate().get(0).getPortfolio()).isEqualTo(purchasePortfolio);
                    assertThat(result.ordersToTrade()).isEmpty();
                });
    }

    @Test
    public void should_create_orders_if_exceed_price_limit() {
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        Portfolio purchasePortfolio = new Portfolio(2L, 2L, 1L);
        new MatchTestBuilder()
                .withPurchaser(purchasePortfolio)
                .withBook(List.of(
                        new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(104), sellerPortfolio, SELL)
                ))
                .whenMatch("1", "103", LIMIT_ORDER)
                .then(result -> {
                    assertThat(result.ordersToCreate()).hasSize(1);
                    assertThat(result.ordersToCreate()).hasSize(1);
                    assertThat(result.ordersToCreate().get(0).getPrice()).isEqualTo(BigDecimal.valueOf(103));
                    assertThat(result.ordersToCreate().get(0).getQuantity()).isEqualTo(BigDecimal.valueOf(1));
                    assertThat(result.ordersToCreate().get(0).getType()).isEqualTo(BUY);
                    assertThat(result.ordersToCreate().get(0).getPortfolio()).isEqualTo(purchasePortfolio);
                    assertThat(result.ordersToTrade()).isEmpty();
                });
    }

    @ParameterizedTest
    @EnumSource(OrderRequestType.class)
    void should__partial_match_all_or_none(OrderRequestType type) {
        Portfolio sellerPortfolio = new Portfolio(1L, 1L, 1L);
        new MatchTestBuilder()
                .withBook(List.of(
                        new OrderDto(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(76), sellerPortfolio, SELL),
                        new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(80), sellerPortfolio, SELL),
                        new OrderDto(3L, BigDecimal.valueOf(1), BigDecimal.valueOf(80), sellerPortfolio, SELL)
                ))
                .whenMatch("3", "200", type)
                .then(result -> {
                    assertThat(result.ordersToCreate()).isEmpty();
                    List<OrderTradeDto> orderTradeDtos = result.ordersToTrade();
                    assertThat(orderTradeDtos).hasSize(2);
                    assertThat(orderTradeDtos.get(0).source().getId()).isEqualTo(1);
                    assertThat(orderTradeDtos.get(0).source().getPrice()).isEqualTo(BigDecimal.valueOf(76));
                    assertThat(orderTradeDtos.get(0).quantity()).isEqualTo(BigDecimal.valueOf(1));
                    assertThat(orderTradeDtos.get(1).source().getId()).isEqualTo(2L);
                    assertThat(orderTradeDtos.get(1).source().getPrice()).isEqualTo(BigDecimal.valueOf(80));
                    assertThat(orderTradeDtos.get(1).quantity()).isEqualTo(BigDecimal.valueOf(2));
                });
    }

    static class MatchTestBuilder {
        private Portfolio purchase = new Portfolio(2L, 2L, 1L);
        private Portfolio seller = new Portfolio(1L, 1L, 1L);
        private List<OrderDto> book = List.of(
                new OrderDto(1L, BigDecimal.ONE, BigDecimal.valueOf(100), seller, SELL),
                new OrderDto(2L, BigDecimal.valueOf(3), BigDecimal.valueOf(120), seller, SELL),
                new OrderDto(3L, BigDecimal.ONE, BigDecimal.valueOf(200), seller, SELL)
        );

        public MatchTestBuilder given() {
            return new MatchTestBuilder();
        }

        public MatchTestBuilder withPurchaser(Portfolio p) {
            this.purchase = p;
            return this;
        }

        public MatchTestBuilder withSeller(Portfolio s) {
            this.seller = s;
            return this;
        }

        public MatchTestBuilder withBook(List<OrderDto> book) {
            this.book = book;
            return this;
        }

        public MatchResultWrapper whenMatch(String quantity, String price, OrderRequestType strategy) {
            var request = new OrderRequest(purchase, new BigDecimal(quantity), new BigDecimal(price), strategy);
            return new MatchResultWrapper(match(book, MatchPolicyFactory.create(request)));
        }
    }

    record MatchResultWrapper(MatchResult result) {
        void then(Consumer<MatchResult> assertions) {
            assertions.accept(result);
        }
    }

}