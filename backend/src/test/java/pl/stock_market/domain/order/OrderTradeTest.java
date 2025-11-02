package pl.stock_market.domain.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.stock_market.config.TestContainersConfiguration;

import java.util.Set;

@SpringBootTest
@Import(TestContainersConfiguration.class)
public class OrderTradeTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void test() {
        orderRepository.findByIdIn(Set.of(1L,2L,3L));
    }

}
