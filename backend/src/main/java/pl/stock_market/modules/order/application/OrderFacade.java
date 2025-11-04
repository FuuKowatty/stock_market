package pl.stock_market.modules.order.application;

import pl.stock_market.modules.order.api.dto.OrderRequest;

public interface OrderFacade {
    void buy(OrderRequest orderRequest);
}
