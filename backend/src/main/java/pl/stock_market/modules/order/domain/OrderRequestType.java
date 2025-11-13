package pl.stock_market.modules.order.domain;

public enum OrderRequestType {
    ALL_OR_NONE, // buy all or nothing
    LIMIT_ORDER // buy all you can and create order for rest
}
