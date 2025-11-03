package pl.stock_market.domain.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.stock_market.config.TestContainersConfiguration;
import pl.stock_market.domain.holder.HolderFacade;
import pl.stock_market.domain.order.dto.OrderRequest;
import pl.stock_market.domain.wallet.WalletFacade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestContainersConfiguration.class)
@AutoConfigureMockMvc
public class OrderServiceITest {
    private final static String ORDERS_ENDPOINTS = "/orders";
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OrderController orderController;
    @MockitoBean
    WalletFacade walletFacadeMock;
    @MockitoBean
    HolderFacade holderFacadeMock;

    @BeforeEach
    void seed() {
        Order sellOrder1 = OrderBuilder.anOrder()
                .withQuantity(BigDecimal.valueOf(50))
                .withPortfolio(2L, 1L, 1L)
                .build();
        Order sellOrder2 = OrderBuilder.anOrder()
                .withQuantity(BigDecimal.valueOf(50))
                .withPortfolio(2L, 1L, 1L)
                .build();
        Order sellOrder3 = OrderBuilder.anOrder()
                .withQuantity(BigDecimal.valueOf(50))
                .withPortfolio(2L, 1L, 2L)
                .build();
        orderRepository.saveAll(List.of(sellOrder1, sellOrder2, sellOrder3));
    }

    @AfterEach
    void clear() {
        orderRepository.deleteAll();
    }

    @Test
    void should_buy_order() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1L, 100L);

        // when
        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        //then
        List<Order> orders = orderRepository.findByIdIn(Set.of(1L, 2L, 3L));
        assertThat(orders).hasSize(3)
                .satisfiesExactlyInAnyOrder(
                        order -> {
                            assertThat(order.getId()).isEqualTo(1L);
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
                            assertThat(order.isActivated()).isFalse();
                        },
                        order -> {
                            assertThat(order.getId()).isEqualTo(2L);
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.ZERO);
                            assertThat(order.isActivated()).isFalse();
                        },
                        order -> {
                            assertThat(order.getId()).isEqualTo(3L);
                            assertThat(order.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(50));
                            assertThat(order.isActivated()).isTrue();
                        }
                );
    }

}
