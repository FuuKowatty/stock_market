package pl.stock_market.modules.order;

import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.domain.Order;

public class OrderMapper {
    public static OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getQuantity(),
                order.getPrice(),
                order.getPortfolio(),
                order.getType()
        );
    }
}
