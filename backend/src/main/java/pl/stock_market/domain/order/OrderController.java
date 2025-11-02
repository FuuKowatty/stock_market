package pl.stock_market.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.stock_market.domain.order.dto.OrderRequest;
import pl.stock_market.infrastructure.application.dto.OrderDto;

import java.util.List;

// add api prefix globally
@RestController("/orders")
@RequiredArgsConstructor
class OrderController {

    private final OrderService orderService;

    @GetMapping("/type/buy")
    public List<OrderDto> getOrdersToBuy(@RequestParam Long stockId) {
        return orderService.getOrdersToBuy(stockId);
    }

    @PostMapping("/type/buy/{quantity}")
    public void buyOrders(@RequestBody OrderRequest orderRequest) {
        orderService.buy(orderRequest);
    }

}
