package pl.stock_market.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class OrderFacadeImpl implements OrderFacade{

    private final OrderRepository orderRepository;

    @Override
    public void afterTrade(Map<Long, BigDecimal> orderQuantities) {
        List<Order> orders = orderRepository.findByIdIn(orderQuantities.keySet());
        // validate if all of them are to buy
        for (Order order : orders) {
            BigDecimal newQuantity = orderQuantities.get(order.getId());
            if (newQuantity != null) {
                order.setQuantity(newQuantity);
                if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
                    order.setActive(false);
                }
            }
        }
        orderRepository.saveAll(orders);
    }

}
