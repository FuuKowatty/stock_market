package pl.stock_market.modules.order.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.stock_market.modules.order.domain.OrderQueryService;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;
import pl.stock_market.modules.order.domain.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController {

    private final OrderService service;
    private final OrderQueryService query;

    @GetMapping("/type/buy")
    public List<OrderDto> getOrdersToBuy(@RequestParam Long stockId) {
        return query.getOrdersToBuy(stockId);
    }

    @PostMapping("/type/buy")
    public void buyOrders(@RequestBody OrderRequest orderRequest) {
        service.buy(orderRequest);
    }

}
