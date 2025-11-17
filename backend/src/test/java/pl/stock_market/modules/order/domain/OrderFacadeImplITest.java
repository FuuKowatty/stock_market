package pl.stock_market.modules.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.stock_market.config.TestContainersConfiguration;
import pl.stock_market.modules.holder.HolderFacade;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.wallet.WalletFacade;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestContainersConfiguration.class)
@AutoConfigureMockMvc
public class OrderFacadeImplITest {
    private final static String ORDERS_ENDPOINTS = "/orders";
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    WalletFacade walletFacadeMock;
    @MockitoBean
    HolderFacade holderFacadeMock;

    @BeforeEach
    void seed() {
        Order sellOrder1 = OrderBuilder.anOrder()
                .withQuantity(BigDecimal.valueOf(5))
                .withPrice("99")
                .withPortfolio(2L, 1L, 2L)
                .withType(Order.OrderType.SELL)
                .build();
        Order sellOrder2 = OrderBuilder.anOrder()
                .withQuantity(BigDecimal.valueOf(4))
                .withPrice("101")
                .withPortfolio(2L, 1L, 1L)
                .withType(Order.OrderType.SELL)
                .build();
        Order sellOrder3 = OrderBuilder.anOrder()
                .withQuantity(BigDecimal.valueOf(1))
                .withPrice("102")
                .withPortfolio(2L, 1L, 1L)
                .withType(Order.OrderType.SELL)
                .build();

        orderRepository.saveAll(List.of(sellOrder1, sellOrder2, sellOrder3));
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("TRUNCATE TABLE orders RESTART IDENTITY CASCADE");
    }

    @Test
    void should_buy_order() throws Exception {
        //given
        Portfolio purchaser = new Portfolio(1L, 1L, 1L);
        OrderRequest orderRequest = new OrderRequest(purchaser, BigDecimal.valueOf(5), BigDecimal.valueOf(102), OrderRequestType.ALL_OR_NONE);

        // when
        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        //then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(3)
                .satisfiesExactlyInAnyOrder(
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("99");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(5));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("101");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
                            assertThat(order.isActivated()).isFalse();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("102");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
                            assertThat(order.isActivated()).isFalse();
                        }
                );
    }

    @Test
    void should_partially_buy_order() throws Exception {
        //given
        Portfolio purchaser = new Portfolio(1L, 1L, 2L);
        OrderRequest orderRequest = new OrderRequest(purchaser, BigDecimal.valueOf(4), BigDecimal.valueOf(99), OrderRequestType.ALL_OR_NONE);

        // when
        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        //then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(3)
                .satisfiesExactlyInAnyOrder(
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("99");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(1));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("101");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(4));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("102");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ONE);
                            assertThat(order.isActivated()).isTrue();
                        }
                );
    }

    @Test
    void should_not_match_order_because_not_enough_money() throws Exception {
        //given
        Portfolio purchaser = new Portfolio(1L, 1L, 1L);
        OrderRequest orderRequest = new OrderRequest(purchaser, BigDecimal.valueOf(5), BigDecimal.valueOf(101), OrderRequestType.ALL_OR_NONE);

        // when
        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        //then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(3)
                .satisfiesExactlyInAnyOrder(
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("99");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(5));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("101");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(4));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("102");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ONE);
                            assertThat(order.isActivated()).isTrue();
                        }
                );
    }

    @Test
    void should_not_match_order_because_not_enough__quantity() throws Exception {
        //given
        Portfolio purchaser = new Portfolio(1L, 1L, 1L);
        OrderRequest orderRequest = new OrderRequest(purchaser, BigDecimal.valueOf(6), BigDecimal.valueOf(102), OrderRequestType.ALL_OR_NONE);

        // when
        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        //then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(3)
                .satisfiesExactlyInAnyOrder(
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("99");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(5));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("101");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(4));
                            assertThat(order.isActivated()).isTrue();
                        },
                        order -> {
                            assertThat(order.getPrice()).isEqualByComparingTo("102");
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ONE);
                            assertThat(order.isActivated()).isTrue();
                        }
                );
    }

}
