package pl.stock_market.modules.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.stock_market.config.BaseIntegrationTest;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.shared.dto.Portfolio;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.stock_market.modules.order.domain.OrderRequestType.ALL_OR_NONE;
import static pl.stock_market.modules.order.domain.OrderRequestType.LIMIT_ORDER;

@Transactional
@WithMockUser(value = "spring")
public class OrderServiceIT extends BaseIntegrationTest {
    private final static String ORDERS_ENDPOINTS = "/orders";
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_create_order() throws Exception {
        Portfolio purchaser = Portfolio.fromId("u1:w1:s1");
        var request = new OrderRequest(purchaser, BigDecimal.valueOf(10), BigDecimal.valueOf(100), LIMIT_ORDER);

        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(orderRepository.findAll())
                .hasSize(1)
                .first()
                .satisfies(order -> {
                    assertThat(order.getQuantity()).isEqualByComparingTo("10");
                    assertThat(order.getType()).isEqualTo(Order.OrderType.BUY);
                });
        verifyNoInteractions(walletFacadeMock, holderFacadeMock);
    }

    @Test
    void should_trade_order() throws Exception {
        Portfolio purchaser = Portfolio.fromId("u1:w1:s1");
        Order newOrder = OrderBuilder.anOrder()
                .withPrice("100")
                .withQuantity("10")
                .withType(Order.OrderType.SELL)
                .withPortfolio(purchaser)
                .build();
        var sellOrder = orderRepository.save(newOrder);
        var request = new OrderRequest(purchaser, new BigDecimal("10"), new BigDecimal("100"), ALL_OR_NONE);

        mockMvc.perform(post(ORDERS_ENDPOINTS + "/type/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(orderRepository.findById(sellOrder.getId())).get()
                .satisfies(order -> {
                    assertThat(order.getQuantity()).isEqualByComparingTo("0");
                    assertThat(order.isActivated()).isFalse();
                });
        verify(walletFacadeMock).doOperations(argThat(w -> w.get(0).amount().compareTo(new BigDecimal("1000")) == 0));
        verify(holderFacadeMock).updatePortfolio(any());
    }

}
