package pl.stock_market.modules.order.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.stock_market.modules.order.application.OrderFacade;
import pl.stock_market.modules.order.application.OrderQueryService;
import pl.stock_market.modules.order.api.dto.OrderDto;
import pl.stock_market.modules.order.api.dto.OrderRequest;

import java.util.List;

// @TODO add api prefix globally
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController {

    private final OrderFacade facade;
    private final OrderQueryService query;

    @GetMapping("/type/buy")
    public List<OrderDto> getOrdersToBuy(@RequestParam Long stockId) {
        return query.getOrdersToBuy(stockId);
    }

    @PostMapping("/type/buy")
    public void buyOrders(@RequestBody OrderRequest orderRequest) {
        facade.buy(orderRequest);
    }

}
